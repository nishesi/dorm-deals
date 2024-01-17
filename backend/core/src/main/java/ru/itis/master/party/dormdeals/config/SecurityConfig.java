package ru.itis.master.party.dormdeals.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.itis.master.party.dormdeals.security.filters.JwtAuthenticationFilter;
import ru.itis.master.party.dormdeals.security.filters.JwtAuthorizationFilter;
import ru.itis.master.party.dormdeals.security.filters.JwtLogoutFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationProvider refreshTokenAuthenticationProvider;

    @Bean
    SecurityFilterChain chain(HttpSecurity http,
                              JwtAuthenticationFilter jwtAuthenticationFilter,
                              JwtAuthorizationFilter jwtAuthorizationFilter,
                              JwtLogoutFilter jwtLogoutFilter
    ) throws Exception {
        return http
                .cors(configurer -> {
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLogoutFilter, LogoutFilter.class)
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requests -> requests
                        // Author

                        .requestMatchers("/auth/token", "/email/confirm/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")

                        // personal

                        .requestMatchers(HttpMethod.GET, "/my/cart").permitAll()
                        .requestMatchers("/my/cart/**", "/my/favorites/**").hasRole("USER")

                        // resources

                        .requestMatchers("/resources/**").permitAll()

                        // shops and products

                        .requestMatchers(HttpMethod.GET, "/products/*", "/shops/*").permitAll()
                        .requestMatchers("/shops/**", "/products/**").hasRole("SELLER")


                        // search

                        .requestMatchers("/search/**").permitAll()

                        // orders

                        .requestMatchers("/orders/**").authenticated()

                        // notifications

                        .requestMatchers("/notifications/**").authenticated()

                        // base pages

                        .requestMatchers("/", "/home").permitAll()

                        // development

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                )
                .headers(configurer -> configurer
                        .xssProtection(c -> {
                        })
                        .contentSecurityPolicy(c -> c
                                .policyDirectives("script-src 'self'")))
                .build();
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(refreshTokenAuthenticationProvider)
                .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
