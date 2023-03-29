package ru.itis.master.party.dormdeals.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenException extends AuthenticationException {
    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
