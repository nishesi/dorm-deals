package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;

import java.io.IOException;


@RequestMapping("/resources")
public interface ResourceApi {
//    @Operation(summary = "Загрузка файла")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Успешная загрузка файлов",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = MultipartFile.class))
//                    }
//            ),
//            @ApiResponse(responseCode = "422", description = "невалидные данные",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ValidationErrorsDto.class))
//                    }
//            )
//    })
//    @PostMapping("/{typeDto}/upload/{id}")
//    ResponseEntity<String> uploadResource(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id, @PathVariable("typeDto") String typeDto);
//
//    @Operation(summary = "Загрузка файлов для товара")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Успешная загрузка файлов",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = MultipartFile.class))
//                    }
//            ),
//            @ApiResponse(responseCode = "422", description = "невалидные данные",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ValidationErrorsDto.class))
//                    }
//            )
//    })
//    @PostMapping("/product/upload/{id}")
//    ResponseEntity<String> uploadProductResource(@RequestParam("files") MultipartFile[] files, @PathVariable("id") Long id);

    @Operation(summary = "Получение файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "невалидные данные"),
            @ApiResponse(responseCode = "200", description = "файл",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = byte[].class))
                    }
            ),
            @ApiResponse(responseCode = "206", description = "часть файла",
                    headers = {
                            @Header(name = "Content-Range")
                    },
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = byte[].class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "файл не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
    })
    @GetMapping("{file-type}/{entity-type}/{file-id}")
    ResponseEntity<byte[]> downloadResource(@PathVariable("file-type") String fileType,
                                            @PathVariable("entity-type") String dtoType,
                                            @PathVariable("file-id") String fileId,
                                            @RequestHeader(required = false, value = "Range") String range) throws IOException;
}
