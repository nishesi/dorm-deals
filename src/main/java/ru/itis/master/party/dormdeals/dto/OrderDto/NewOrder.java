package ru.itis.master.party.dormdeals.dto.OrderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Новый заказ")
public class NewOrder {

    @Schema(description = "идентификатор магазина", example = "100500")
    private Long shopId;

    @Schema(description = "комментарий пользователя", example = "а можно шоколадку в подарок :)")
    private String userComment;
}
