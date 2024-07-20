package com.devmare.tradingbackend.business.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );
}
