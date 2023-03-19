package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ResourceUrlResolver resolver;
    public UserDto from(User user) {
        String imageUrl = resolver.resolveUrl(user.getId().toString(), ResourceType.USER_IMAGE);
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .dormitory(user.getDormitory())
                .userImageUrl(imageUrl)
                .build();
    }
}
