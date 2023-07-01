package ru.itis.master.party.dormdeals.services.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.ResourceDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.services.ResourceService;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    @Value("${storage.path}")
    private String storagePath;

    @Value("${resource.max-return-size}")
    private int maxReturnSize;

    private static String validateExtension(FileType fileType,
                                            MultipartFile multipartFile) throws NotAcceptableException {
        String type = multipartFile.getContentType();
        if (type == null)
            throw new NotAcceptableException("empty content type");

        String[] parts = type.split("/");

        if (!fileType.extensions().contains(parts[1])) {
            throw new NotAcceptableException("Not acceptable type");
        }
        return parts[1];
    }

    private static byte[] readBytes(File file, long offset, int len) {
        byte[] buff = new byte[len];

        try (var rea = new RandomAccessFile(file, "r")) {
            rea.seek(offset);
            int read = rea.read(buff);

            if (read == buff.length) {
                return buff;
            }
            return Arrays.copyOf(buff, read);
        } catch (IOException ex) {
            throw new RuntimeException("io problem", ex);
        }
    }


    @Nonnull
    private File getFile(FileType fileType, EntityType entityType, String id) throws NotFoundException {
        for (String ext : fileType.extensions()) {
            File file = Paths.get(storagePath, fileType.name(), entityType.name(), id + "." + ext).toFile();
            if (file.exists() && file.isFile()) {
                return file;
            }
        }
        throw new NotFoundException(File.class, "id", id);
    }

    @Override
    public void saveFile(FileType fileType, EntityType entityType, String id, MultipartFile multipartFile) {
        // Delete another files that saved by this id
        try {
            File file = getFile(fileType, entityType, id);
            file.delete();
        } catch (NotFoundException ignored) {
        }

        String extension = validateExtension(fileType, multipartFile);
        String fileName = id + "." + extension;
        File file = Paths.get(storagePath, fileType.name(), entityType.name(), fileName).toFile();

        try {
            file.createNewFile();
            try (var out = new BufferedOutputStream(new FileOutputStream(file))) {
                multipartFile.getInputStream().transferTo(out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(FileType fileType, EntityType entityType, String id) {
        try {
            File file = getFile(fileType, entityType, id);
            file.delete();
        } catch (NotFoundException ignored) {
        }
    }

    @Override
    public ResourceDto getResource(FileType fileType, EntityType entityType, String id) {
        File file = getFile(fileType, entityType, id);
        byte[] content = (file.length() > maxReturnSize)
                ? readBytes(file, 0, maxReturnSize)
                : readBytes(file, 0, (int) file.length());
        return new ResourceDto(file, 0, content.length - 1, content);
    }

    @Override
    public ResourceDto getResourceInRange(FileType fileType, EntityType entityType, String id, String range) {
        if (range == null) throw new IllegalArgumentException("range cant be null");
        File file = getFile(fileType, entityType, id);

        String[] input = range.split("[=-]");

        if (input.length < 2) {
            throw new IllegalArgumentException("bad range header");
        }

        long start = Long.parseLong(input[1]);
        int len = maxReturnSize;

        if (input.length >= 3) {
            long end = Long.parseLong(input[2]);
            int requestedLen = (int) (end - start + 1);
            if (requestedLen < maxReturnSize) len = requestedLen;
        }

        byte[] bytes = readBytes(file, start, len);
        return new ResourceDto(file, start, start + bytes.length - 1, bytes);
    }
}
