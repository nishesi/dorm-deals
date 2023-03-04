package ru.itis.master.party.dormdeals.services.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.FileDto.FileLinkDto;
import ru.itis.master.party.dormdeals.models.FileInfo;
import ru.itis.master.party.dormdeals.repositories.FilesInfoRepository;
import ru.itis.master.party.dormdeals.services.FilesService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    @Value("${storage.location}")
    private String storagePath;

    private final FilesInfoRepository filesInfoRepository;

    @Transactional
    @Override
    public FileLinkDto upload(MultipartFile multipart) {
        try {
            String extension = multipart.getOriginalFilename().substring(multipart.getOriginalFilename().lastIndexOf("."));
            String storageFileName = UUID.randomUUID() + extension;

            FileInfo file = FileInfo.builder()
                    .type(multipart.getContentType())
                    .originalFileName(multipart.getOriginalFilename())
                    .storageFileName(storageFileName)
                    .size(multipart.getSize())
                    .build();

            Files.copy(multipart.getInputStream(), Paths.get(storagePath, file.getStorageFileName()));

            filesInfoRepository.save(file);

            return FileLinkDto.builder()
                    .link(storagePath + file.getStorageFileName())
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot upload file.");
        }
    }

    @Override
    public void addFileToResponse(String fileName, HttpServletResponse response) {

    }
}
