package ru.itis.master.party.dormdeals.dto.OrderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @Positive(message = "{constraint.id.positive.message}")
    private Long shopId;

    @Schema(description = "комментарий пользователя", example = "а можно шоколадку в подарок :)")
    @Size(max = 255)
    private String userComment;
}
