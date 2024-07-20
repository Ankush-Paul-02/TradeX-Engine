package com.devmare.tradingbackend.data.utils;


import com.devmare.tradingbackend.data.enums.VerificatonType;

public class AppUtils {

    public static String generateOtp() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append((int) (Math.random() * 10));
        }
        return otp.toString();
    }

    public static boolean isVerificationTypeValid(String verificatonType) {
        for (VerificatonType type : VerificatonType.values()) {
            if (type.name().equals(verificatonType.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
