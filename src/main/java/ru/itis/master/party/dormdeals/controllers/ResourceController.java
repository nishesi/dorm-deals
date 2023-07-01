package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ResourceApi;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.ResourceDto;
import ru.itis.master.party.dormdeals.services.ResourceService;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class ResourceController implements ResourceApi {
    private final ResourceService resourceService;

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> handle(NoSuchElementException ex) {
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(ex.getMessage(), 400));
    }

    @Override
    public ResponseEntity<byte[]> downloadResource(String fileType, String dtoType,
                                                   String fileId, String rangeValue) throws IOException {
        FileType fileTypeEnum = FileType.from(fileType);
        EntityType entityTypeEnum = EntityType.from(dtoType);

        ResourceDto resource = (rangeValue == null)
                ? resourceService.getResource(fileTypeEnum, entityTypeEnum, fileId)
                : resourceService.getResourceInRange(fileTypeEnum, entityTypeEnum, fileId, rangeValue);
        File file = resource.file();
        HttpStatus httpStatus = (resource.end() < file.length() - 1) ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;

        return ResponseEntity.status(httpStatus)
                .header("Content-Type", Files.probeContentType(file.toPath()))
                .header("Content-Length", String.valueOf(resource.content().length))
                .header("Content-Range", "bytes " + resource.start() + "-" + resource.end() + "/" + file.length())
                .body(resource.content());
    }

//    @Override
//    public ResponseEntity<String> uploadResource(MultipartFile file, Long id, String typeDto) {
//
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("No file provided.", HttpStatus.BAD_REQUEST);
//        }
//
//
//        resourceService.uploadResource(file, id, File.FileDtoType.valueOf(typeDto.toUpperCase()));
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @Override
//    public ResponseEntity<String> uploadProductResource(MultipartFile[] files, Long id) {
//        if (files.length == 0) {
//            return new ResponseEntity<>("No file provided.", HttpStatus.BAD_REQUEST);
//        }
//        resourceService.uploadResource(files, id, File.FileDtoType.PRODUCT);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }


//    @Override
//    public ResponseEntity<Resource> downloadResource(String fileType,
//                                                     String dtoType,
//                                                     Long id) throws IOException {
//
//        String fileName = resourceService.getFileName(dtoType, id);
//
//        Path filePath = Paths.get(storagePath, dtoType, fileName);
//
//        byte[] fileData = Files.readAllBytes(filePath);
//
//        ByteArrayResource resource = new ByteArrayResource(fileData);
//
//        String mimeType = Files.probeContentType(filePath);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(mimeType));
//        headers.setContentDispositionFormData("attachment", fileName);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(resource);
//    }


//    @Override
//    public ResponseEntity<Resource> downloadResource(String fileType,
//                                                           Long id,
//                                                     Integer numberResource) throws IOException {
//
//
//        String fileName = resourceService.getFileName(id, numberResource);
//
//        Path filePath = Paths.get(storagePath, "product", fileName);
//
//        byte[] fileData = Files.readAllBytes(filePath);
//
//        ByteArrayResource resource = new ByteArrayResource(fileData);
//
//        String mimeType = Files.probeContentType(filePath);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(mimeType));
//        headers.setContentDispositionFormData("attachment", fileName);
//
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(resource);
//    }
}
