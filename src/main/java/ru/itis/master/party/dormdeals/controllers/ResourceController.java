package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.ResourceApi;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.services.ResourceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class ResourceController implements ResourceApi {
    private final ResourceService resourceService;
    @Value("${storage.path}")
    private String storagePath;

    @Override
    public ResponseEntity<String> uploadResource(MultipartFile file, Long id, String typeDto) {

        if (file.isEmpty()) {
            return new ResponseEntity<>("No file provided.", HttpStatus.BAD_REQUEST);
        }


        resourceService.uploadResource(file, id, File.FileDtoType.valueOf(typeDto.toUpperCase()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<String> uploadProductResource(MultipartFile[] files, Long id) {
        if (files.length == 0) {
            return new ResponseEntity<>("No file provided.", HttpStatus.BAD_REQUEST);
        }
        resourceService.uploadResource(files, id, File.FileDtoType.PRODUCT);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Override
    public ResponseEntity<Resource> downloadResource(String fileType,
                                                     String dtoType,
                                                     Long id) throws IOException {

        String fileName = resourceService.getFileName(dtoType, id);

        Path filePath = Paths.get(storagePath, dtoType, fileName);

        byte[] fileData = Files.readAllBytes(filePath);

        ByteArrayResource resource = new ByteArrayResource(fileData);

        String mimeType = Files.probeContentType(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    @Override
    public ResponseEntity<Resource> downloadResource(String fileType,
                                                           Long id,
                                                     Integer numberResource) throws IOException {


        String fileName = resourceService.getFileName(id, numberResource);

        Path filePath = Paths.get(storagePath, "product", fileName);

        byte[] fileData = Files.readAllBytes(filePath);

        ByteArrayResource resource = new ByteArrayResource(fileData);

        String mimeType = Files.probeContentType(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", fileName);


        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
