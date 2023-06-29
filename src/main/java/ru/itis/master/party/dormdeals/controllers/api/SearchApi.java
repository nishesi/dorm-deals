package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;

import java.util.List;

@Tags(value = {
        @Tag(name = "Search")
})
@RequestMapping("/search")
public interface SearchApi {
    @Operation(summary = "Поиск товаров по критериям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары найдены"),
            @ApiResponse(responseCode = "404", description = "Таких товаров нет",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @GetMapping("/products")
    ResponseEntity<List<ProductDto>> searchProducts(
            @Parameter(description = "Список категорий и списки значений", example = "name-query=Lays,cheetos&category=chips")
            @RequestParam MultiValueMap<String, String> criteria,
            @Parameter(description = "Номер страницы", example = "1")
            @RequestParam(value = "pageIndex",
                    required = false,
                    defaultValue = "0")
            Integer pageIndex);

}
