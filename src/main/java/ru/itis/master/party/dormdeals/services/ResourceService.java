package ru.itis.master.party.dormdeals.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.models.File;

import java.util.List;

public interface ResourceService {
    void uploadResource(MultipartFile file, Long id, File.FileDtoType dtoType);
    void uploadResource(MultipartFile[] files, Long id, File.FileDtoType dtoType);

    String getFileName(String dtoType, Long id);
    String getFileName(Long id, Integer numberResource);
}
