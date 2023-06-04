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
        http
                .csrf().disable()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLogoutFilter, LogoutFilter.class)
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requests -> requests
                                .shouldFilterAllDispatcherTypes(false)

                                // User

                                .requestMatchers("/auth/token", "/email/confirm/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/logout").permitAll()
                                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                                .requestMatchers("/my/favourites/**").hasAnyRole("USER", "SELLER")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/my/cart/**").permitAll()

                                // business logic

//                        .requestMatchers(HttpMethod.GET, "/products/**", "/shops/**").permitAll()
                                .requestMatchers("/products/**", "/shops/**").hasRole("SELLER")
                                .requestMatchers(HttpMethod.GET,"/resource/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/resource/**").hasRole("SELLER")

                                // base pages

                                .requestMatchers("/", "/home").permitAll()

                                // development

                                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                )
                .headers().xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");

        return http.build();
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(refreshTokenAuthenticationProvider)
                .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
