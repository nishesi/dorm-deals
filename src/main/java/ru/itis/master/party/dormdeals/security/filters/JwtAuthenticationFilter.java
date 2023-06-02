package ru.itis.master.party.dormdeals.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.services.JwtService;
import ru.itis.master.party.dormdeals.security.authentication.RefreshAuthenticationToken;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.security.service.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_PARAMETER = "email";
    public static final String AUTHENTICATION_URL = "/auth/token";
    private final AuthorizationHeaderUtil authorizationHeaderUtil;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;


    public JwtAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                   AuthorizationHeaderUtil authorizationHeaderUtil,
                                   ObjectMapper objectMapper,
                                   JwtService jwtService,
                                   JwtUtil jwtUtil
    ) throws Exception {
        super(authenticationConfiguration.getAuthenticationManager());
        this.setUsernameParameter(USERNAME_PARAMETER);
        this.setFilterProcessesUrl(AUTHENTICATION_URL);

        this.authorizationHeaderUtil = authorizationHeaderUtil;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        if (authorizationHeaderUtil.hasAuthorizationToken(request)) {

            String refreshToken = authorizationHeaderUtil.getToken(request);
            RefreshAuthenticationToken authentication = new RefreshAuthenticationToken(refreshToken);
            return getAuthenticationManager().authenticate(authentication);
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("application/json");

        var userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String email = userDetails.getUsername();
        String issuer = request.getRequestURL().toString();

        List<String> authorities = userDetails.getUser().getAuthorities().stream().map(Enum::toString).toList();
        Map<String, String> tokens = jwtUtil.generateTokens(email, authorities, issuer);

        jwtService.addTokensToUser(email, tokens.get("accessToken"), tokens.get("refreshToken"));

        objectMapper.writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
