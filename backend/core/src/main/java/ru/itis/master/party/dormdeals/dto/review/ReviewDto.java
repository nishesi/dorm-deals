package ru.itis.master.party.dormdeals.dto.review;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Отзыв")
public class ReviewDto {

    private Long id;

    @Schema(description = "пользователь который оставляет отзыв")
    private Author author;

    @Schema(description = "товар на который оставлен отзыв")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Product product;

    @Schema(description = "содержание отзыва")
    private String message;

    @Schema(description = "выставленная оценка от 1 до 5")
    private int score;

    public record Author(
            Long id,

            @Schema(example = "Bob")
            String firstName,

            @Schema(example = "Martin")
            String lastName
    ) {
    }

    public record Product(
            Long id,
            String name
    ) {
    }
}
