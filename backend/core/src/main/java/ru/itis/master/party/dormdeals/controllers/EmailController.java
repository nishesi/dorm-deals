package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.services.UserService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final UserService userService;
    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("accept") String hashForConfirm) {
        userService.activateAccount(hashForConfirm);
        URI location = URI.create("/");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
