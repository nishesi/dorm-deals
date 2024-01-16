package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ResourceUrlResolver resolver;
    public UserDto from(User user) {
        String imageUrl = resolver.resolveUrl(FileType.IMAGE, EntityType.USER, String.valueOf(user.getId()));
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .resourceUrl(imageUrl)
                .build();
    }
}
