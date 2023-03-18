package ru.itis.master.party.dormdeals.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "user")
public class UserDto {
    @Schema(description = "почта пользователя", example = "email@gmail.com")
    private String email;
    @Schema(description = "пароль пользователя", example = "1234")
    private String password;
    @Schema(description = "имя пользователя", example = "Bob")
    private String firstName;
    @Schema(description = "фамилия пользователя", example = "Martin")
    private String lastName;
    @Schema(description = "телефонный номер пользователя", example = "89993335566")
    private String telephone;
    @Schema(description = "место проживания пользователя", example = "Universiade Village, 18")
    private String dormitory;
    @Schema(description = "url фото пользователя", example = "https://resource/1235lk1425lkj")
    private String userImageUrl;

    public static UserDto from(User user, ResourceUrlResolver resolver) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .dormitory(user.getDormitory())
                .userImageUrl(resolver.resolveUrl(user.getId().toString(), ResourceType.USER_IMAGE))
                .build();
    }
}
