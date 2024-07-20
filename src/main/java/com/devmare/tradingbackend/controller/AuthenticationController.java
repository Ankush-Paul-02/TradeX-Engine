package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.LoginRequestDto;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<DefaultResponse> register(
            @RequestBody User user
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                authenticationService.register(user)
                        ),
                        "User registered successfully!"
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResponse> login(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                authenticationService.login(loginRequestDto)
                        ),
                        "User logged in successfully!"
                )
        );
    }

    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<DefaultResponse> verifyOtp(
            @PathVariable String otp,
            @RequestParam Long verificationId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                authenticationService.verifyOtp(otp, verificationId)
                        ),
                        "OTP verified successfully!"
                )
        );
    }
}
