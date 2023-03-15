package ru.itis.master.party.dormdeals.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.security.utils.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.models.Token;
import ru.itis.master.party.dormdeals.repositories.TokenRepository;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ru.itis.master.party.dormdeals.security.filters.TokenAuthenticationFilter.AUTHENTICATION_URL;

@Component
@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        if (!request.getServletPath().equals(AUTHENTICATION_URL) &&
                authorizationHeaderUtil.hasAuthorizationToken(request)
        ) {
            String outerToken = authorizationHeaderUtil.getToken(request);
            Optional<Token> tokenOptional = tokenRepository.findById(outerToken);

            if (tokenOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Token token = tokenOptional.get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new UserDetailsImpl(User.builder()
                            .email(token.getEmail())
                            .role(token.getRole())
                            .build()),
                    null,
                    List.of(new SimpleGrantedAuthority(token.getRole().toString())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
