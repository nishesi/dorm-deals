package ru.itis.master.party.dormdeals.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static ru.itis.master.party.dormdeals.enums.EntityType.USER;
import static ru.itis.master.party.dormdeals.enums.FileType.IMAGE;

@Mapper(componentModel = SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class UserMapper {

    @Autowired
    protected ResourceUrlResolver resolver;

    @Mapping(target = "resourceUrl", source = "id", qualifiedByName = "userIdToResourceUrl")
    public abstract UserDto toUserDto(User user);

    @Named("userIdToResourceUrl")
    protected String toResourceUrl(Long userId) {
        return resolver.resolveUrl(IMAGE, USER, userId.toString());
    }
}
