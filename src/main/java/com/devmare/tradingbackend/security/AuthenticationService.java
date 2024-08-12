package com.devmare.tradingbackend.security;

import com.devmare.tradingbackend.business.dto.AuthResponseDto;
import com.devmare.tradingbackend.business.dto.LoginRequestDto;
import com.devmare.tradingbackend.business.service.EmailService;
import com.devmare.tradingbackend.business.service.TwoFactorOtpService;
import com.devmare.tradingbackend.data.entity.RefreshToken;
import com.devmare.tradingbackend.data.entity.TwoFactorAuthentication;
import com.devmare.tradingbackend.data.entity.TwoFactorOtp;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.enums.Provider;
import com.devmare.tradingbackend.data.enums.UserRole;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.data.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final TwoFactorOtpService twoFactorOtpService;
    private final EmailService emailService;


    public AuthResponseDto register(User newUserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(newUserRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new UserInfoException("User already exists with email: " + newUserRequest.getEmail());
        }

        if (newUserRequest.getEmail() == null || newUserRequest.getEmail().isEmpty()) {
            throw new UserInfoException("Email is mandatory");
        }
        if (newUserRequest.getFullname() == null || newUserRequest.getFullname().isEmpty()) {
            throw new UserInfoException("Name is mandatory");
        }
        if (newUserRequest.getPassword() == null || newUserRequest.getPassword().isEmpty()) {
            throw new UserInfoException("Password is mandatory");
        }
        if (newUserRequest.getPhone() == null || newUserRequest.getPhone().isEmpty()) {
            throw new UserInfoException("Phone is mandatory");
        }

        TwoFactorAuthentication twoFactorAuthentication = TwoFactorAuthentication.builder()
                .isEnabled(false)
                .build();

        User newUser = User.builder()
                .fullname(newUserRequest.getFullname())
                .email(newUserRequest.getEmail())
                .role(UserRole.ROLE_CUSTOMER)
                .provider(Provider.EMAIL_PASSWORD)
                .twoFactorAuthentication(twoFactorAuthentication)
                .phone("+91" + newUserRequest.getPhone())
                .password(passwordEncoder.encode(newUserRequest.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        String accessToken = jwtService.generateToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .isTwoFactorAuthEnabled(false)
                .build();
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User with email " + loginRequestDto.getEmail() + " does not exist");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserInfoException("Invalid password for user with email " + loginRequestDto.getEmail());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        String accessToken = jwtService.generateToken(new HashMap<>(), user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        if (user.getTwoFactorAuthentication().getIsEnabled()) {

            String generatedOtp = AppUtils.generateOtp();

            TwoFactorOtp optionalTwoFactorOtp = twoFactorOtpService.getTwoFactorOtpByUser(user.getId());

            if (optionalTwoFactorOtp != null) {
                twoFactorOtpService.deleteTwoFactorOtpById(optionalTwoFactorOtp.getId());
            }

            twoFactorOtpService.createTwoFactorOtp(
                    user.getId(),
                    generatedOtp,
                    accessToken
            );

            emailService.sendEmail(
                    user.getEmail(),
                    "Trading App - Two Factor Authentication",
                    "Your OTP is: " + generatedOtp
            );

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .isTwoFactorAuthEnabled(true)
                    .build();
        }
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponseDto verifyOtp(
            String otp,
            Long verificationId
    ) {
        TwoFactorOtp twoFactorOtp = twoFactorOtpService.getTwoFactorOtpById(verificationId);
        if (twoFactorOtp == null) {
            throw new UserInfoException("Invalid OTP verification id");
        }

        if (twoFactorOtpService.verifyTwoFactorOtp(otp, verificationId)) {
            return AuthResponseDto.builder()
                    .accessToken(twoFactorOtp.getJwt())
                    .refreshToken(refreshTokenService.createRefreshToken(twoFactorOtp.getUser().getEmail()).getRefreshToken())
                    .build();
        }
        throw new UserInfoException("Invalid OTP");
    }

    public User findAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return userRepository.findByEmail(currentUserName).orElseThrow(() ->
                new UserInfoException("User " + currentUserName + " not found"));
    }

    public boolean isPasswordValid(User user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
