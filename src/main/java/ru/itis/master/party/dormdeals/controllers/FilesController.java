package ru.itis.master.party.dormdeals.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.FileDto.FileLinkDto;
import ru.itis.master.party.dormdeals.services.FilesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;

    @PostMapping("/upload")
    public ResponseEntity<FileLinkDto> upload(@Parameter(description = "The file to upload", required = true)
                                                  @RequestPart(value = "file")
                                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
                                                          MultipartFile multipart) {
        return ResponseEntity.ok(filesService.upload(multipart));
    }

    @GetMapping("/{file-name:.+}")
    public void getFile(@PathVariable("file-name") String fileName, HttpServletResponse response) {
        filesService.addFileToResponse(fileName, response);
    }


}
