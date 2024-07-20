package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.TwoFactorOtp;
import com.devmare.tradingbackend.data.entity.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(
            Long userId,
            String otp,
            String jwt
    );

    TwoFactorOtp getTwoFactorOtpByUser(Long userId);

    TwoFactorOtp getTwoFactorOtpById(Long twoFactorOtpId);

    void deleteTwoFactorOtpById(Long twoFactorOtpId);

    boolean verifyTwoFactorOtp(String otp, Long twoFactorOtpId);
}
