package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.services.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    @GetMapping("/confirm_account/{email}/{code}")
    public ResponseEntity<?> confirm(@PathVariable("code") String code,
                                     @PathVariable("email") String email) {
        emailService.confirmAccount(email, code);
        return ResponseEntity.accepted().build();
    }
}
