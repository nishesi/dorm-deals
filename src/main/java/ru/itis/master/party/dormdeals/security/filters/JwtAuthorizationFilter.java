package ru.itis.master.party.dormdeals.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.master.party.dormdeals.security.service.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;

import java.io.IOException;

import static ru.itis.master.party.dormdeals.security.filters.JwtAuthenticationFilter.AUTHENTICATION_URL;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(AUTHENTICATION_URL)) {
            filterChain.doFilter(request, response);
        } else {
            if (authorizationHeaderUtil.hasAuthorizationToken(request)) {
                String jwt = authorizationHeaderUtil.getToken(request);
                try {
                    Authentication authentication = jwtUtil.buildAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException e) {
                    logger.info(e.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else{
                filterChain.doFilter(request, response);
            }
        }
    }
}
