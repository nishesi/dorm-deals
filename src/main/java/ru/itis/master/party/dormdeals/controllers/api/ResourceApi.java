package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

import java.io.IOException;


@RequestMapping("/resource")
@Tags(value = {
        @Tag(name = "Resources")
})
public interface ResourceApi {
    @Operation(summary = "Загрузка файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешная загрузка файлов",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MultipartFile.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping("/{typeDto}/upload/{id}")
    ResponseEntity<String> uploadResource(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id, @PathVariable("typeDto") String typeDto);

    @Operation(summary = "Загрузка файлов для товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешная загрузка файлов",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MultipartFile.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping("/product/upload/{id}")
    ResponseEntity<String> uploadProductResource(@RequestParam("files") MultipartFile[] files, @PathVariable("id") Long id);

    @Operation(summary = "Получение файлов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файлы",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Resource.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("{fileType}/{dtoType}/{id}")
    ResponseEntity<Resource> downloadResource(@PathVariable("fileType") String fileType,
                                                     @PathVariable("dtoType") String dtoType,
                                                     @PathVariable("id") Long id) throws IOException;

    @Operation(summary = "Получение файлов товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получение файлов",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Resource.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("{fileType}/product/{id}/{numberResource}")
    ResponseEntity<Resource> downloadResource(@PathVariable("fileType") String fileType,
                                                     @PathVariable("id") Long id,
                                                     @PathVariable("numberResource") Integer numberResource) throws IOException;
}
