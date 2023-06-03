package ru.itis.master.party.dormdeals.utils;

import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.models.File;

@Component
public class ResourceUrlResolver {

    public String resolveUrl(Long id, File.FileDtoType dtoType,  File.FileType fileType) {
        return "http://localhost/resource/" + fileType + "/" + dtoType + "/" + id;
    }

    public String resolveUrl(Long id, File.FileDtoType dtoType, File.FileType fileType, Integer numberResource) {
        return "http://localhost/resource/" + fileType + "/" + dtoType + "/" + id + "/" + numberResource;
    }
}
