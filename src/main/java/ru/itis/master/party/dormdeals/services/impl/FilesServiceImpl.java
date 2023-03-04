package ru.itis.master.party.dormdeals.services.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.FileDto.FileLinkDto;
import ru.itis.master.party.dormdeals.services.FilesService;

public class FilesServiceImpl implements FilesService {
    @Override
    public FileLinkDto upload(MultipartFile multipart, String description) {
        return null;
    }

    @Override
    public void addFileToResponse(String fileName, HttpServletResponse response) {

    }
}
