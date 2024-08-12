package com.devmare.tradingbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @GetMapping
    public String home(
            @AuthenticationPrincipal OAuth2User user,
            Model model
    ) {
        log.info("Home page accessed");
        if (user != null) {
            log.info("User info: {}", user.getAttributes());
            return "Hello " + user.getAttribute("name") + user.getAttribute("email");
        } else {
            return "Hello Guest";
        }
    }
}
