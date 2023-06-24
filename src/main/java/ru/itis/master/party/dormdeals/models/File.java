package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    private FileDtoType fileDtoType;

    public enum FileType {
        IMAGE,
        VIDEO
    }

    public enum FileDtoType {
        PRODUCT,
        USER,
        SHOP
    }
}
