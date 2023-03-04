package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.FileInfo;

import java.util.Optional;

public interface FilesInfoRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByStorageFileName(String fileName);
}

