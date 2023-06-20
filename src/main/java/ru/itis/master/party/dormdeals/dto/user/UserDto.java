package ru.itis.master.party.dormdeals.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "user")
public class UserDto {

    @Schema(description = "почта пользователя", example = "email@gmail.com")
    private String email;

    @Schema(description = "имя пользователя", example = "Bob")
    private String firstName;

    @Schema(description = "фамилия пользователя", example = "Martin")
    private String lastName;

    @Schema(description = "телефонный номер пользователя", example = "89993335566")
    private String telephone;

//    @Schema(description = "url фото пользователя", example = "https://resource/1235lk1425lkj")
//    private String userImageUrl;
    private String resourceUrl;
}
