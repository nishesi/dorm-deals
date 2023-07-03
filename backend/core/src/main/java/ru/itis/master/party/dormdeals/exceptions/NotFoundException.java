package ru.itis.master.party.dormdeals.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private Class<?> clazz;
    private String queryParam;
    private Object queryValue;

    @Override
    public String getMessage() {
        return clazz.getName() + " with " + queryParam + " <" + queryValue + "> not found";
    }
}
