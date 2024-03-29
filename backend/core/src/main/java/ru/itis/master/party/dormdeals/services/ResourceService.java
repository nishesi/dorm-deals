package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.ResourceDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;

public interface ResourceService {
    void saveFile(
            FileType fileType, EntityType entityType, String id, MultipartFile file
    ) throws NotFoundException, NotAcceptableException;

    void deleteFile(FileType fileType, EntityType entityType, String id);

    ResourceDto getResource(
            FileType fileType, EntityType entityType, String id
    ) throws NotFoundException;

    ResourceDto getResourceInRange(
            FileType fileType, EntityType entityType, String id, String range
    ) throws NotFoundException;
}
