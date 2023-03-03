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
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.*;

@Tags(value = {
        @Tag(name = "Products")
})
@RequestMapping("/product")
public interface ProductApi {

    @Operation(summary = "Получение списка продуктов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с продуктами",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductsPage.class))
                    })
    })
    @GetMapping
    ResponseEntity<ProductsPage> getAllProducts(@Parameter(description = "Номер страницы") @RequestParam("page") int page);


    @Operation(summary = "Добавление нового продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный продукт",
                    content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    @PostMapping
    ResponseEntity<ProductDto> addProduct(@Parameter(description = "Данные нового товара") @RequestBody NewProduct newProduct);


    @Operation(summary = "Получение товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о товаре",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })

    @GetMapping("/{product-id}")
    ResponseEntity<ProductDto> getProduct(@Parameter(description = "Получение товара по идентификатору", example = "1")
                                          @PathVariable("product-id") Long productId);

    @Operation(summary = "Обновление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновлённый товар",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateProduct.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{product-id}")
    ResponseEntity<ProductDto> updateProduct(@Parameter(description = "Обновление товара") @PathVariable("product-id") Long productId,
                                             @RequestBody UpdateProduct updatedProduct);


    @Operation(summary = "Удаление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "товар удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })

    @DeleteMapping("/{product-id}")
    ResponseEntity<?> deleteProduct(@Parameter(description = "Удаление товара", example = "1") @PathVariable("product-id") Long productId);

//    @PutMapping("/{product-id}")
//    ResponseEntity<ProductDto> returnProductInSell(@Parameter(description = "Возвращение товара в продажу", example = "1")
//                                                   @PathVariable("product-id") Long productId);
}
