package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.TwoFactorOtpService;
import com.devmare.tradingbackend.data.entity.TwoFactorOtp;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.TwoFactorOtpRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    private final TwoFactorOtpRepository twoFactorOtpRepository;
    private final UserRepository userRepository;

    @Override
    public TwoFactorOtp createTwoFactorOtp(
            Long userId,
            String otp,
            String jwt
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();

        TwoFactorOtp twoFactorOtp = TwoFactorOtp.builder()
                .user(user)
                .otp(otp)
                .jwt(jwt)
                .build();
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    @Override
    public TwoFactorOtp getTwoFactorOtpByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<TwoFactorOtp> optionalTwoFactorOtp = twoFactorOtpRepository.findByUser(user);

        return twoFactorOtpRepository.findByUser(user).orElse(null);
    }

    @Override
    public TwoFactorOtp getTwoFactorOtpById(Long twoFactorOtpId) {
        Optional<TwoFactorOtp> optionalTwoFactorOtp = twoFactorOtpRepository.findById(twoFactorOtpId);
        if (optionalTwoFactorOtp.isEmpty()) {
            throw new UserInfoException("Two factor otp not found");
        }
        return optionalTwoFactorOtp.get();
    }

    @Override
    public void deleteTwoFactorOtpById(Long twoFactorOtpId) {
        Optional<TwoFactorOtp> optionalTwoFactorOtp = twoFactorOtpRepository.findById(twoFactorOtpId);
        if (optionalTwoFactorOtp.isEmpty()) {
            throw new UserInfoException("Two factor otp not found");
        }
        twoFactorOtpRepository.delete(optionalTwoFactorOtp.get());
    }

    @Override
    public boolean verifyTwoFactorOtp(String otp, Long twoFactorOtpId) {
        Optional<TwoFactorOtp> optionalTwoFactorOtp = twoFactorOtpRepository.findById(twoFactorOtpId);
        if (optionalTwoFactorOtp.isEmpty()) {
            throw new UserInfoException("Two factor otp not found");
        }
        TwoFactorOtp twoFactorOtp = optionalTwoFactorOtp.get();
        return twoFactorOtp.getOtp().equals(otp);
    }
}
