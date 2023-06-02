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
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtilImpl implements JwtUtil {

    private static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofSeconds(10 * 60);
    private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofSeconds(20 * 60);

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Map<String, String> generateTokens(String subject, List<String> authorities, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));


        String accessToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION.toMillis()))
                .withClaim("authorities", authorities)
                .withIssuer(issuer)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION.toMillis()))
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

    @Override
    public Jwt from(String token) {
        ParsedToken parsedToken = parse(token);
        Duration expiration = Duration.ofMillis(parsedToken.expiresAt.getTime() - System.currentTimeMillis());
        if (expiration.isNegative()) expiration = Duration.ZERO;

        return Jwt.builder()
                .token(token)
                .expiration(expiration)
                .build();
    }

    private ParsedToken parse(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        String email = decodedJWT.getSubject();
        Date date = decodedJWT.getExpiresAt();
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);

        return ParsedToken.builder()
                .authorities(authorities)
                .expiresAt(date)
                .email(email)
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class ParsedToken {
        private String email;
        private Date expiresAt;
        private List<String> authorities;
    }
}
