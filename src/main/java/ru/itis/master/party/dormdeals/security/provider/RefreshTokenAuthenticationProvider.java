package ru.itis.master.party.dormdeals.security.provider;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.security.authentication.RefreshTokenAuthentication;
import ru.itis.master.party.dormdeals.security.exceptions.RefreshTokenException;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;


@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String refreshTokenValue = (String) authentication.getCredentials();
        try {
            return jwtUtil.buildAuthentication(refreshTokenValue);
        } catch (JWTVerificationException e) {
            log.info(e.getMessage());
            throw new RefreshTokenException(e.getMessage(), e);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
