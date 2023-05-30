package ru.itis.master.party.dormdeals.security.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface JwtUtil {
    Map<String, String> generateTokens(String subject, List<String> authorities, String issuer);

    Authentication buildAuthentication(String token) throws JWTVerificationException;
}
