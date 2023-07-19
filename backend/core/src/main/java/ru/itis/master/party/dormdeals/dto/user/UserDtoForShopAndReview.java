package ru.itis.master.party.dormdeals.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.itis.master.party.dormdeals.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoForShopAndReview {
    private Long id;

    @Schema(example = "Bob")
    private String firstName;

    @Schema(example = "Martin")
    private String lastName;

    public static UserDtoForShopAndReview from(User user) {
        return UserDtoForShopAndReview.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
