package ru.itis.master.party.dormdeals.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotEnoughException extends RuntimeException {
    private Class<?> element;
    private Object id;
    private int required;
    private int available;

    @Override
    public String getMessage() {
        return element.getName() + "with id " + id + " requested " + required + ", but available " + available;
    }
}
