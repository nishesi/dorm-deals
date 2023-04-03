package ru.itis.master.party.dormdeals.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.security.details.UserDetailsServiceImpl;
import ru.itis.master.party.dormdeals.security.filters.JwtAuthenticationFilter;
import ru.itis.master.party.dormdeals.security.filters.JwtAuthorizationFilter;


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
                              JwtAuthorizationFilter jwtAuthorizationFilter
    ) throws Exception {
        http
                .csrf().disable()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests((requests) -> requests
                        .shouldFilterAllDispatcherTypes(false)

                        // User

                        .requestMatchers("/auth/token", "/email/confirm/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/my/favourites/**").hasAnyRole("USER", "SELLER")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/my/cart/**").permitAll()

                        // business logic

//                        .requestMatchers(HttpMethod.GET, "/products/**", "/shops/**").permitAll()
                        .requestMatchers("/products/**", "/shops/**").hasRole("ROLE_SELLER")
                        .requestMatchers("/products/**", "/shops/**").permitAll()

                        // base pages

                        .requestMatchers("/", "/home").permitAll()

                        // development

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .addLogoutHandler(tokenLogoutHandler))
                .headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");

        return http.build();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(refreshTokenAuthenticationProvider);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
