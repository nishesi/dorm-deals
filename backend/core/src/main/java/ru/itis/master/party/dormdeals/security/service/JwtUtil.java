package ru.itis.master.party.dormdeals.security.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;
import ru.itis.master.party.dormdeals.models.jpa.User;

import java.util.Map;

public interface JwtUtil {
    Map<String, String> generateTokens(User user, String issuer);

    Authentication buildAuthentication(String token) throws JWTVerificationException;
    Jwt from(String token);
}
