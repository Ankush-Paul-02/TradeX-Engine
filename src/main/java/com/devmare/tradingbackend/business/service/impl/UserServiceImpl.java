package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.dto.UpdatePasswordDto;
import com.devmare.tradingbackend.business.service.UserService;
import com.devmare.tradingbackend.data.entity.TwoFactorAuthentication;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.enums.VerificatonType;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.data.utils.AppUtils;
import com.devmare.tradingbackend.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findAuthenticatedUser() {
        return authenticationService.findAuthenticatedUser();
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        return optionalUser.get();
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        return optionalUser.get();
    }

    @Override
    public User enableTwoFactorAuthentication(Long userId, String verificatonType) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();

        if (verificatonType == null || !AppUtils.isVerificationTypeValid(verificatonType)) {
            throw new UserInfoException("Invalid verification type");
        }

        TwoFactorAuthentication twoFactorAuthentication =
                TwoFactorAuthentication.builder()
                        .isEnabled(true)
                        .sendTo(VerificatonType.valueOf(verificatonType))
                        .build();
        user.setTwoFactorAuthentication(twoFactorAuthentication);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(UpdatePasswordDto updatePasswordDto) {
        Optional<User> optionalUser = userRepository.findById(updatePasswordDto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();

        if (!authenticationService.isPasswordValid(user, updatePasswordDto.getCurrentPassword())) {
            throw new UserInfoException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        return userRepository.save(user);
    }
}
