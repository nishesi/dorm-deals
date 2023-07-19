package ru.itis.master.party.dormdeals.security.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderUtilImpl implements AuthorizationHeaderUtil {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public boolean hasAuthorizationToken(HttpServletRequest request) {
        String value = request.getHeader(AUTHORIZATION_HEADER_NAME);
        return value != null && value.startsWith(BEARER);
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER_NAME)
                .substring(BEARER.length());
    }
}