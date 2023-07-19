package ru.itis.master.party.dormdeals.security.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationHeaderUtil {
    boolean hasAuthorizationToken(HttpServletRequest request);
    String getToken(HttpServletRequest request);
}