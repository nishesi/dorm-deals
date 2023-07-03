package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;

import java.io.IOException;

@RequestMapping("/resource")
@Tags(value = {
        @Tag(name = "Resources")
})
public interface ResourceApi {

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
