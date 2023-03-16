package ru.itis.master.party.dormdeals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

@SpringBootApplication
public class DormDealsApplication {
    @Bean
    @Primary
    PasswordEncoder passwordEncoder(@Value("${password.encoder.strength}") int value) {
        return new BCryptPasswordEncoder(value);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    public static void main(String[] args) {
        SpringApplication.run(DormDealsApplication.class, args);
    }
}
