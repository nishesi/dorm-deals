package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.aspects.RestExceptionHandler.ExceptionDto;
import ru.itis.master.party.dormdeals.controllers.api.ResourceApi;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.services.ResourceService;

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
}
