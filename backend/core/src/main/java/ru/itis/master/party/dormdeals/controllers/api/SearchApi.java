package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.master.party.dormdeals.dto.CatalogueElastic;
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
            @ApiResponse(responseCode = "200", description = "Товары найдены",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto[].class))
                    }),
            @ApiResponse(responseCode = "404", description = "Таких товаров нет",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @GetMapping("/products")
    ResponseEntity<List<ProductDto>> searchProducts(
            @Parameter(description = "Список категорий и списки значений",
                    example = """
                            {"name-query":["Товар","Cheetos"],"category":"chips"}
                            """)
            @RequestParam MultiValueMap<String, String> criteria,
            @PageableDefault
            Pageable pageable);


    @Operation(description = "Найти каталоги и товары с ценами соответствующего региона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результаты поиска",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка обработки",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content)
    })
    @GetMapping("/by-text")
    List<CatalogueElastic> searchByText(
            @Parameter(description = "Поисковый запрос")
            @RequestParam
            String text
    );
}
