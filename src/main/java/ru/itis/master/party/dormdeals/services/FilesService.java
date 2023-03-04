package ru.itis.master.party.dormdeals.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.FileDto.FileLinkDto;

public interface FilesService {
    FileLinkDto upload(MultipartFile multipart, String description);

    void addFileToResponse(String fileName, HttpServletResponse response);
}
