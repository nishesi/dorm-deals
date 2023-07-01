package ru.itis.master.party.dormdeals.utils;

import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;

@Component
public class ResourceUrlResolver {

    public String resolveUrl(FileType fileType, EntityType entityType, String id) {
        return "http://localhost/resources/" + fileType.type() + "/" + entityType.entity() + "/" + id;
    }
}
