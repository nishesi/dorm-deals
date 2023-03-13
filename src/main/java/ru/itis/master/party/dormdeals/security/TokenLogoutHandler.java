package ru.itis.master.party.dormdeals.security;

import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.repositories.TokenRepository;
import ru.itis.master.party.dormdeals.security.utils.AuthorizationHeaderUtil;

@Component
@RequiredArgsConstructor
public class TokenLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final AuthorizationHeaderUtil authorizationHeaderUtil;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = authorizationHeaderUtil.getToken(request);
        tokenRepository.deleteById(token);
    }
}
