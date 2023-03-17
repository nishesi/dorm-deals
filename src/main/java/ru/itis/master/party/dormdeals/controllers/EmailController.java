package ru.itis.master.party.dormdeals.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.services.EmailService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("accept") String hashForConfirm) {
        emailService.confirmAccount(hashForConfirm);
        URI location = URI.create("/");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
