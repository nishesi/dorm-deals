package ru.itis.master.party.dormdeals.security.filters;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.master.party.dormdeals.redis.JwtService;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.security.service.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final AuthorizationHeaderUtil authorizationHeaderUtil;
    private final RequestMatcher requestMatcher = AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/logout");

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if (requestMatcher.matches(request)) {
            if (authorizationHeaderUtil.hasAuthorizationToken(request)) {
                String token = authorizationHeaderUtil.getToken(request);
                Authentication authentication = jwtUtil.buildAuthentication(token);

                String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
                jwtService.blockAllTokensForUser(email);

                response.setStatus(HttpServletResponse.SC_ACCEPTED);

            } else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else filterChain.doFilter(request, response);
    }
}
