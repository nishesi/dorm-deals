package ru.itis.master.party.dormdeals.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtilImpl implements JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRES_TIME = 1000 * 60 * 24;

    private static final long REFRESH_TOKEN_EXPIRES_TIME = 1000 * 60 * 24 * 10;

    //TODO: перенести secret key в переменную окружения
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Map<String, String> generateTokens(String subject, List<String> authorities, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        String accessToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME))
                .withClaim("authorities", authorities)
                .withIssuer(issuer)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRES_TIME))
                .withClaim("authorities", authorities)
                .withIssuer(issuer)
                .sign(algorithm);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);
    }

    @Override
    public Authentication buildAuthentication(String token) throws JWTVerificationException {
        ParsedToken parsedToken = parse(token);
        List<Authority> authorities = parsedToken.getAuthorities().stream().map(Authority::valueOf).toList();

        UserDetails userDetails = new UserDetailsImpl(
                User.builder()
                        .authorities(authorities)
                        .email(parsedToken.getEmail())
                        .build());

        List<SimpleGrantedAuthority> grantedAuthorities = parsedToken.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
    }

    private ParsedToken parse(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        String email = decodedJWT.getSubject();
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);

        return ParsedToken.builder()
                .authorities(authorities)
                .email(email)
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class ParsedToken {
        private String email;
        private List<String> authorities;
    }
}
