package ru.itis.master.party.dormdeals.dto.review;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "новый отзыв")
public class NewReviewDto {
    @Schema(example = "хороший товар")
    @NotBlank
    @Length(min = 5, max = 500)
    private String message;
    @Min(1)
    @Max(5)
    private int score;
}
