package ru.itis.master.party.dormdeals.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.converters.UserConverter;
import ru.itis.master.party.dormdeals.security.authentication.RefreshAuthenticationToken;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.security.service.AuthorizationHeaderUtil;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;
import ru.itis.master.party.dormdeals.services.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_PARAMETER = "email";
    public static final String AUTHENTICATION_URL = "/auth/token";
    private final AuthorizationHeaderUtil authorizationHeaderUtil;
    private final UserConverter userConverter;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    @Value("${password.salt}")
    private String salt;


    public JwtAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                   AuthorizationHeaderUtil authorizationHeaderUtil,
                                   UserConverter userConverter,
                                   ObjectMapper objectMapper,
                                   JwtService jwtService,
                                   JwtUtil jwtUtil
    ) throws Exception {
        super(authenticationConfiguration.getAuthenticationManager());
        this.setUsernameParameter(USERNAME_PARAMETER);
        this.setFilterProcessesUrl(AUTHENTICATION_URL);

        this.authorizationHeaderUtil = authorizationHeaderUtil;
        this.userConverter = userConverter;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        for (Cookie cookie : request.getCookies() != null ? request.getCookies() : new Cookie[0]) {
            if (cookie.getName().equals("refreshToken")) {
                String refreshToken = cookie.getValue();
                RefreshAuthenticationToken authentication = new RefreshAuthenticationToken(refreshToken);
                return getAuthenticationManager().authenticate(authentication);
            }
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return super.obtainPassword(request) + salt;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("application/json");
        var userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String issuer = request.getRequestURL().toString();
        Map<String, String> tokens = jwtUtil.generateTokens(userDetails.getUser(), issuer);

        String email = userDetails.getUsername();
        jwtService.addTokensToUser(email, tokens.get("accessToken"), tokens.get("refreshToken"));

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.get("refreshToken"));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath(request.getContextPath() + request.getServletPath());
        response.addCookie(refreshTokenCookie);

        Map<String, Object> resp = new HashMap<>(tokens);
        resp.put("user", userConverter.from(userDetails.getUser()));
        objectMapper.writeValue(response.getOutputStream(), resp);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
