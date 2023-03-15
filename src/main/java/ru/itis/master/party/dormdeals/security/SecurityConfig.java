package ru.itis.master.party.dormdeals.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.master.party.dormdeals.security.details.UserDetailsServiceImpl;
import ru.itis.master.party.dormdeals.security.filters.TokenAuthenticationFilter;
import ru.itis.master.party.dormdeals.security.filters.TokenAuthorizationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    SecurityFilterChain chain(HttpSecurity http,
                              TokenAuthenticationFilter authenticationFilter,
                              TokenAuthorizationFilter authorizationFilter,
                              TokenLogoutHandler tokenLogoutHandler) throws Exception {
        http
                .csrf().disable()
                .addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests((requests) -> requests
                        .shouldFilterAllDispatcherTypes(false)

                        // User

                        .requestMatchers("/auth/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")

                        // business logic

                        .requestMatchers(HttpMethod.GET, "/products/**", "/shops/**").permitAll()
                        .requestMatchers("/products/**", "/shops/**").hasRole("SELLER")

                        // base pages

                        .requestMatchers("/", "/home").permitAll()

                        // development

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(tokenLogoutHandler));

        return http.build();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
