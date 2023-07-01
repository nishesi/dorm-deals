package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.*;
import ru.itis.master.party.dormdeals.dto.product.NewProduct;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductsPage;
import ru.itis.master.party.dormdeals.dto.product.UpdateProduct;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

@Tags(value = {
        @Tag(name = "Products")
})
@RequestMapping("/products")
public interface ProductApi {


//    @Operation(summary = "Получение списка продуктов c идентификатором магазина")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Страница с продуктами",
//                    content = {
//                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductsPage.class))
//                    })
//    })
//    @GetMapping("/shop/{shop-id}")
//    ResponseEntity<ProductsPage> getAllProductsByShop(@Parameter(description = "Номер страницы") @RequestParam("page") int page, @Parameter(description = "Идентификатор магазина") @PathVariable("shop-id") Long shopId);

    @Operation(summary = "Получение списка продуктов без идентификатора магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с продуктами",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductsPage.class))
                    }
            )
    })
    @GetMapping
    ResponseEntity<ProductsPage> getAllProducts(
            @Parameter(description = "Номер страницы")
            @RequestParam("page")
            int page);

    @Operation(summary = "Добавление нового продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный продукт",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<ProductDto> addProduct(
            @Parameter(description = "Данные нового товара")
            @RequestBody @Valid
            NewProduct newProduct,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

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
    ResponseEntity<ProductDto> getProduct(
            @Parameter(description = "Получение товара по идентификатору", example = "1")
            @PathVariable("product-id")
            Long productId);

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
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PutMapping("/{product-id}")
    ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Обновление товара")
            @PathVariable("product-id")
            Long productId,
            @RequestBody
            UpdateProduct updatedProduct,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Удаление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{product-id}")
    ResponseEntity<?> deleteProduct(
            @Parameter(description = "Удаление товара", example = "1")
            @PathVariable("product-id")
            Long productId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Возврат товара в продажу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар возвращен"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{product-id}/restore")
    ResponseEntity<ProductDto> returnProductInSell(
            @Parameter(description = "Возвращение товара в продажу", example = "1")
            @PathVariable("product-id")
            Long productId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "добавление картинок к товару")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = "multipart/form-data",
            schema = @Schema(type = "object"),
            schemaProperties = {
                    @SchemaProperty(name = "file",
                            schema = @Schema(type = "string", format = "binary"))
            }
    ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "создано")
    })
    @PostMapping("{product-id}/images")
    ResponseEntity<?> addProductImage(
            @PathVariable("product-id")
            Long productId,
            @Parameter(description = "новая картинка")
            @RequestParam("file")
            MultipartFile file,
            @Parameter(hidden = true) UserDetailsImpl userDetails);

    @Operation(summary = "удаление картинки товара")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "удалено"),
            @ApiResponse(responseCode = "404", description = "не найдено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
            ),
            @ApiResponse(responseCode = "406", description = "нет доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("{product-id}/images/{image-id}")
    ResponseEntity<?> deleteProductImage(
            @Parameter(description = "идентификатор товара")
            @PathVariable("product-id")
            Long productId,
            @Parameter(description = "идентификатор картинки")
            @PathVariable("image-id")
            String imageId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);
}
