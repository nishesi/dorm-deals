package ru.itis.master.party.dormdeals.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.itis.master.party.dormdeals.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoForShop {
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
