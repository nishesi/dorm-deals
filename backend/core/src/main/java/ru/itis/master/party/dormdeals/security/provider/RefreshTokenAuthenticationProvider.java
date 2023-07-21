package ru.itis.master.party.dormdeals.security.provider;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.security.authentication.RefreshAuthenticationToken;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.security.exceptions.RefreshTokenException;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;
import ru.itis.master.party.dormdeals.services.JwtService;


@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String refreshTokenValue = (String) authentication.getCredentials();
            if (jwtService.isBlockedRefreshToken(refreshTokenValue)) {
                throw new RefreshTokenException("Token blocked");
            }
            Authentication authentication1 = jwtUtil.buildAuthentication(refreshTokenValue);
            long userId = ((UserDetailsImpl) authentication1.getPrincipal()).getUser().getId();
            UserDetailsImpl userDetails = new UserDetailsImpl(userRepository.findById(userId).orElseThrow());

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (JWTVerificationException e) {
            log.info(e.getMessage());
            throw new RefreshTokenException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
