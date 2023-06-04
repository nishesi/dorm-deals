package ru.itis.master.party.dormdeals;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication(exclude = FreeMarkerAutoConfiguration.class)
public class DormDealsApplication {

    @Bean
    Configuration configuration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/ftlh/");
        return configuration;
    }
    @Bean
    @Primary
    PasswordEncoder passwordEncoder(@Value("${password.encoder.strength}") int value) {
        return new BCryptPasswordEncoder(value);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Tika tika() {
        return new Tika();
    }

    public static void main(String[] args) {
        SpringApplication.run(DormDealsApplication.class, args);
    }
}
