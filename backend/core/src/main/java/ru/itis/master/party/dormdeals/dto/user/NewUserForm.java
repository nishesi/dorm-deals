package ru.itis.master.party.dormdeals.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "новый пользователь")
public record NewUserForm(

        @Schema(example = "email@gmail.com")
        @NotBlank
        @Email
        String email,

        @Schema(example = "12345678")
        @NotBlank
        @Length(min = 8, max = 50)
        String password,

        @Schema(example = "Bob")
        @NotBlank
        @Length(max = 50)
        String firstName,

        @Schema(example = "Martin")
        @NotBlank
        @Length(max = 50)
        String lastName,

        @Schema(example = "89993335566")
        @NotBlank
        @Pattern(regexp = "[+]?[0-9]{11}", message = "{constraint.telephone.message}")
        String telephone
) {
}
