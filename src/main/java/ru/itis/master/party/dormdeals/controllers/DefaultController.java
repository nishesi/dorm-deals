package ru.itis.master.party.dormdeals.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping("/")
    public String getDefault() {
        return "Default page";
    }

    @GetMapping("/home")
    public String getHome() {
        return "Home page";
    }

    @GetMapping("/post")
    public String getPost() {
        return "Post page";
    }
    @PostMapping("/post")
    public String doPost() {
        return "Post accepted";
    }
}
