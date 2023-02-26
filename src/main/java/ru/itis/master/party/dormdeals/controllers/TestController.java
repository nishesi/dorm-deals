package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.dto.TestDto;
import ru.itis.master.party.dormdeals.services.TestService;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    @GetMapping("/test/{id}")
    public TestDto test(@PathVariable long id) {
        return testService.getTestDto(id);
    }
}
