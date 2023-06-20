package ru.itis.master.party.dormdeals.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.itis.master.party.dormdeals.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoForShop {
    private Long id;

    @Schema(example = "Bob")
    private String firstName;

    @Schema(example = "Martin")
    private String lastName;

    public static UserDtoForShop from(User user) {
        return UserDtoForShop.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
