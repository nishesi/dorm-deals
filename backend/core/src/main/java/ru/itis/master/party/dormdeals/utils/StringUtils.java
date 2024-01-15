package ru.itis.master.party.dormdeals.utils;

import java.util.List;

public class StringUtils {
    private static final char[] RU = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-',
            'й', 'ц', 'у', 'к', 'е', 'н', 'г', 'ш', 'щ', 'з', 'х', 'ъ',
            'ф', 'ы', 'в', 'а', 'п', 'р', 'о', 'л', 'д', 'ж', 'э',
            'я', 'ч', 'с', 'м', 'и', 'т', 'ь', 'б', 'ю', '.',
            ' ',};
    private static final char[] EN = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']',
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '"',
            'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/',
            ' '};

    public static Boolean isContainErrorChar(String text) {
        return text.contains("[") || text.contains("]")
                || text.contains("\"") || text.contains("/")
                || text.contains(";");
    }

    public static String convert(String message) {
        char[] from;
        char[] to;

        if (message.matches(".*\\p{InCyrillic}.*")) {
            from = RU;
            to = EN;
        } else {
            from = EN;
            to = RU;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int j = 0; j < EN.length; j++) {
                if (message.charAt(i) == from[j]) {
                    builder.append(to[j]);
                    break;
                }
            }
        }
        return builder.toString();
    }

    public static boolean parseAndAssertNeedConvert(String text, List<String> words) {
        boolean needConvert = true;
        if (isContainErrorChar(text)) {
            text = convert(text);
            needConvert = false;
        } else if (isContainErrorChar(convert(text))) {
            needConvert = false;
        }
        words.addAll(List.of(text.split(" ")));
        return needConvert;
    }

    public static String createQuery(String word, boolean needConvert) {
        return word + ((needConvert) ? " " + convert(word) : "");
    }
}
