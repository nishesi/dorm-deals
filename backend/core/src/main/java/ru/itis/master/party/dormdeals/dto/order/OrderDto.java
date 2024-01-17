package ru.itis.master.party.dormdeals.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Заказ")
public class OrderDto {

    @Schema(description = "Идентификатор заказа", example = "1")
    private Long id;

    @Schema(description = "Заказчик")
    private Customer customer;

    @Schema(description = "Магазин")
    private Shop shop;

    @Schema(description = "информация о содержимом заказа")
    private List<Product> products;

    @Schema(description = "история переписки закзачика и продавца")
    private Page<Message> messages;

    @Schema(description = "Время заказа", example = "16-03-2023 21:40")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm-ss XX")
    private ZonedDateTime addedDate;

    @Schema(description = "Стоимость заказа")
    private float price;

    public record Customer(
            Long id,
            String firstName,
            String lastName
    ) {
    }

    public record Shop(
            Long id,
            String name
    ) {
    }

    public record Product(
            Long id,
            String name,
            Integer count,
            Float price
    ) {
    }

    public record Message(
            Long userId,
            String message,
            ZonedDateTime addedDate
    ) {
    }
}
