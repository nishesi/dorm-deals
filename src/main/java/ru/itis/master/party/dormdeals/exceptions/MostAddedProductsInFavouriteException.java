package ru.itis.master.party.dormdeals.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class MostAddedProductsInFavouriteException extends RuntimeException{
    public MostAddedProductsInFavouriteException(String message) {
        super(message);
    }
}
