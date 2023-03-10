package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;

public interface Security {
    @PostMapping("/login")
    @Schema(description = "аутентификация пользователя")
    void login(@Parameter(description = "логин") String login, @Parameter(description = "password") String password);
}
