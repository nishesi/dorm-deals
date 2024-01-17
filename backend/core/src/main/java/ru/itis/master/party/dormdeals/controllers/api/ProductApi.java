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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.aspects.RestExceptionHandler.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.product.NewProductForm;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.UpdateProductForm;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

@Tags({
        @Tag(name = "Products")
})
@RequestMapping("/products")
public interface ProductApi {

    @Operation(summary = "Добавление нового продукта")
    @ApiResponses({
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
            ),
            @ApiResponse(responseCode = "404", description = "магазин не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping
    ResponseEntity<ProductDto> addProduct(
            @Parameter(description = "Данные нового товара")
            @RequestBody
            NewProductForm newProductForm,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Получение товара по идентификатору")
    @ApiResponses({
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
            ),
            @ApiResponse(responseCode = "406", description = "Ошибка доступа",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @GetMapping("/{product-id}")
    ResponseEntity<ProductDto> getProduct(
            @Parameter(description = "идентификатор", example = "1")
            @PathVariable("product-id")
            Long productId,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false)
            int pageIndex,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);


    @Operation(summary = "Обновление товара")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Обновлённый товар",
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
            ),
            @ApiResponse(responseCode = "422", description = "Сведения о невалидных данных",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            ),
            @ApiResponse(responseCode = "406", description = "Ошибка доступа",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PutMapping("/{product-id}")
    ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "идентификатор")
            @PathVariable("product-id")
            Long productId,
            @RequestBody
            UpdateProductForm updatedProduct,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Удаление товара")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "406", description = "Ошибка доступа",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @DeleteMapping("/{product-id}")
    ResponseEntity<?> deleteProduct(
            @Parameter(description = "Удаление товара", example = "1")
            @PathVariable("product-id")
            Long productId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Изменение статуса товара")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Статус обновлён"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "406", description = "ошибка выполнения",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PutMapping("/{product-id}/state")
    ResponseEntity<?> updateProductState(
            @Parameter(description = "Идентификатор товара")
            @PathVariable("product-id")
            Long productId,
            @Parameter(description = "Статус товара")
            @RequestParam
            Product.State state,
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
