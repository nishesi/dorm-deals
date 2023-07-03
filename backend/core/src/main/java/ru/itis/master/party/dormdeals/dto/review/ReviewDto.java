package ru.itis.master.party.dormdeals.dto.review;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.product.ProductDtoForReview;
import ru.itis.master.party.dormdeals.dto.user.UserDtoForShopAndReview;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Отзыв")
public class ReviewDto {

    @Schema(description = "пользователь который оставляет отзыв")
    private UserDtoForShopAndReview user;

    @Schema(description = "товар на который оставлен отзыв")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductDtoForReview product;
    @Schema(description = "содержание отзыва")
    private String message;
    @Schema(description = "выставленная оценка от 1 до 5")
    private int score;
}
