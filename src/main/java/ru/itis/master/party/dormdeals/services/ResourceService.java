package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.ResourceDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;

public interface ResourceService {
    void saveFile(FileType fileType, EntityType entityType, String id, MultipartFile file);

    ResourceDto getResource(FileType fileType, EntityType entityType, String id);

    ResourceDto getResourceInRange(FileType fileType, EntityType entityType, String id, String range);
}
