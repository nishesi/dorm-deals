package ru.itis.master.party.dormdeals.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.security.authentication.RefreshTokenAuthentication;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.security.service.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_PARAMETER = "email";
    public static final String AUTHENTICATION_URL = "/auth/token";

    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    private final AuthorizationHeaderUtil authorizationHeaderUtil;


    public JwtAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                   ObjectMapper objectMapper,
                                   JwtUtil jwtUtil,
                                   AuthorizationHeaderUtil authorizationHeaderUtil) throws Exception {
        super(authenticationConfiguration.getAuthenticationManager());
        this.setUsernameParameter(USERNAME_PARAMETER);
        this.setFilterProcessesUrl(AUTHENTICATION_URL);
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.authorizationHeaderUtil = authorizationHeaderUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (authorizationHeaderUtil.hasAuthorizationToken(request)) {
            String refreshToken = authorizationHeaderUtil.getToken(request);
            RefreshTokenAuthentication authentication = new RefreshTokenAuthentication(refreshToken);
            return super.getAuthenticationManager().authenticate(authentication);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setContentType("application/json");

        GrantedAuthority currentAuthority = authResult.getAuthorities().stream().findFirst().orElseThrow();
        String email = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();
        String issuer = request.getRequestURL().toString();

        Map<String, String> tokens = jwtUtil.generateTokens(email, currentAuthority.getAuthority(), issuer);

        objectMapper.writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
