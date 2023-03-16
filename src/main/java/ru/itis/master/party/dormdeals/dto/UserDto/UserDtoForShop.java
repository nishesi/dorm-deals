package ru.itis.master.party.dormdeals.dto.UserDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.itis.master.party.dormdeals.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoForShop {
    @Schema(example = "Bob")
    private String firstName;
    @Schema(example = "Martin")
    private String lastName;
    @Schema(example = "Universiade Village, 18")
    private String dormitory;

    public static UserDtoForShop from(User user) {
        return UserDtoForShop.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dormitory(user.getDormitory())
                .build();
    }

}
