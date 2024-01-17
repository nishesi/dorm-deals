package ru.itis.master.party.dormdeals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import freemarker.template.Configuration;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@EnableScheduling
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
    PasswordEncoder passwordEncoder(@Value("${app.password.encoder.strength}") int value) {
        return new BCryptPasswordEncoder(value);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    Tika tika() {
        return new Tika();
    }

    @Bean
    ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public static void main(String[] args) {
        SpringApplication.run(DormDealsApplication.class, args);
    }
}
