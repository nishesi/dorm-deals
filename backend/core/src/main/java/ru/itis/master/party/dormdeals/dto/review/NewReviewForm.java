package ru.itis.master.party.dormdeals.dto.review;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "новый отзыв")
public record NewReviewForm(

        @Schema(example = "хороший товар")
        @NotBlank
        @Length(min = 5, max = 500)
        String message,

        @Min(1)
        @Max(5)
        int score
) {
}
