package ru.itis.master.party.dormdeals.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "обновлненная информация о пользователе")
public class UpdateUserDto {

    @Schema(example = "12345678")
    @Length(min = 8, max = 50)
    private String password;

    @Schema(example = "Bob")
    @NotBlank
    @Length(max = 50)
    private String firstName;

    @Schema(example = "Martin")
    @NotBlank
    @Length(max = 50)
    private String lastName;

    @Schema(example = "89993335566")
    @NotBlank
    @Pattern(regexp = "[+]?[0-9]{11}", message = "{constraint.pattern.telephone.message}")
    private String telephone;
}
