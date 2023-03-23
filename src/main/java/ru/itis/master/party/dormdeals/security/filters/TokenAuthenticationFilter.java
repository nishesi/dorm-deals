package ru.itis.master.party.dormdeals.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.Token;
import ru.itis.master.party.dormdeals.repositories.TokenRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHENTICATION_URL = "/auth/token";

    private static final String USERNAME_PARAMETER = "email";

    private final ObjectMapper objectMapper;

    private final TokenRepository tokenRepository;

    public TokenAuthenticationFilter(AuthenticationConfiguration configuration, ObjectMapper mapper, TokenRepository tokenRepository) throws Exception {
        super(configuration.getAuthenticationManager());
        setUsernameParameter(USERNAME_PARAMETER);
        setFilterProcessesUrl(AUTHENTICATION_URL);
        this.objectMapper = mapper;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult
    ) throws IOException {
        response.setContentType("application/json");

        Token token = tokenRepository
                .findByEmail(authResult.getName())
                .orElseGet(() -> {
                    List<Authority> authorities = authResult.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(Authority::valueOf)
                            .toList();

                    Token newToken = Token.builder()
                            .token(UUID.randomUUID().toString())
                            .email(authResult.getName())
                            .authorities(authorities)
                            .build();
                    tokenRepository.save(newToken);
                    return newToken;
                });

        objectMapper.writeValue(response.getOutputStream(), Map.of("token", token.getToken()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
