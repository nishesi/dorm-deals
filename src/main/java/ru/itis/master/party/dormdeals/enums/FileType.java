package ru.itis.master.party.dormdeals.enums;

import java.util.List;
import java.util.NoSuchElementException;

public enum FileType {
    IMAGE("image", List.of("jpeg", "png")),
    VIDEO("video", List.of("mp4"));
    private final List<FileType> fileTypes;
    private final String type;
    private final List<String> extensions;

    FileType(String type, List<String> extensions) {
        this.type = type;
        this.extensions = extensions;
        fileTypes = List.of(values());
    }

    public FileType from(String s) {
        for (FileType fileType : fileTypes) {
            if (fileType.type.equals(s)) {
                return fileType;
            }
        }
        throw new NoSuchElementException(FileType.class + " for s = " + s);
    }

    public List<FileType> fileTypes() {
        return fileTypes;
    }

    public String type() {
        return type;
    }

    public List<String> extensions() {
        return extensions;
    }
}
