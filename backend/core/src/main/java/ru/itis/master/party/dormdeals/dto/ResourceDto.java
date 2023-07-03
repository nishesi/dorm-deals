package ru.itis.master.party.dormdeals.dto;

import java.io.File;

/**
 *
 * @param file
 * @param start inclusive
 * @param end inclusive
 * @param content
 */
public record ResourceDto(File file, long start, long end, byte[] content) {
}
