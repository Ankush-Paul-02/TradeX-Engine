package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.UpdatePasswordDto;
import com.devmare.tradingbackend.business.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/find-authenticated-user")
    public ResponseEntity<DefaultResponse> findAuthenticatedUser() {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                userService.findAuthenticatedUser()
                        ),
                        "Authenticated user found successfully!"
                )
        );
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<DefaultResponse> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                userService.findUserByEmail(email)
                        ),
                        "User found by email successfully!"
                )
        );
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<DefaultResponse> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                userService.findUserById(id)
                        ),
                        "User found by id successfully!"
                )
        );
    }

    @PutMapping("/enableTwoFactorAuthentication/{userId}")
    public ResponseEntity<DefaultResponse> enableTwoFactorAuthentication(
            @PathVariable Long userId,
            @RequestParam String verificatonType
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                userService.enableTwoFactorAuthentication(userId, verificatonType)
                        ),
                        "Two factor authentication enabled successfully!"
                )
        );
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<DefaultResponse> updatePassword(
            @RequestBody UpdatePasswordDto updatePasswordDto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        DefaultResponse.Status.SUCCESS,
                        Map.of(
                                "data",
                                userService.updatePassword(updatePasswordDto)
                        ),
                        "Password updated successfully!"
                )
        );
    }
}
