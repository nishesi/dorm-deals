package ru.itis.master.party.dormdeals.dto.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;

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
    private UserDto user;

    @Schema(description = "Магазин")
    private ShopDto shop;

    @Schema(description = "информация о содержимом заказа")
    private List<OrderProductDto> orderProducts;

    @Schema(description = "история переписки закзачика и продавца")
    private List<OrderMessageDto> orderMessages;

    @Schema(description = "Время заказа", example = "16-03-2023 21:40")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm-ss XX")
    private ZonedDateTime addedDate;

    @Schema(description = "Стоимость заказа")
    private float price;
}
