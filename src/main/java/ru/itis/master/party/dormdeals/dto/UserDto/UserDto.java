package ru.itis.master.party.dormdeals.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "user")
public class UserDto {
    @Schema(example = "email@gmail.com")
    @Email
    private String email;
    @Schema(example = "1234")
    private String password;
    @Schema(example = "Bob")
    private String firstName;
    @Schema(example = "Martin")
    private String lastName;
    @Schema(example = "89993335566")
    private String telephone;
    @Schema(example = "Universiade Village, 18")
    private String dormitory;

    public static UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .dormitory(user.getDormitory())
                .build();
    }
}
