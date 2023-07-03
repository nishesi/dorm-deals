package ru.itis.master.party.dormdeals.enums;

import java.util.List;
import java.util.NoSuchElementException;

public enum FileType {
    IMAGE("images", List.of("jpeg", "png")),
    VIDEO("videos", List.of("mp4"));
    private static final List<FileType> FILE_TYPES = List.of(values());
    private final String type;
    private final List<String> extensions;

    FileType(String type, List<String> extensions) {
        this.type = type;
        this.extensions = extensions;
    }

    public static FileType from(String s) {
        for (FileType fileType : FILE_TYPES) {
            if (fileType.type.equals(s)) {
                return fileType;
            }
        }
        throw new NoSuchElementException(FileType.class + " for s = " + s);
    }

    public static List<FileType> fileTypes() {
        return FILE_TYPES;
    }

    public String type() {
        return type;
    }

    public List<String> extensions() {
        return extensions;
    }
}
