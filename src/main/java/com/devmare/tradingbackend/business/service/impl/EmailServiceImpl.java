package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.EmailService;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new UserInfoException("Error while sending email");
        }
    }
}
