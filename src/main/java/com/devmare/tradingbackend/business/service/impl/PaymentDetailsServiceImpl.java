package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.dto.PaymentDetailsDto;
import com.devmare.tradingbackend.business.service.PaymentDetailsService;
import com.devmare.tradingbackend.data.entity.PaymentDetails;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.PaymentDetailsRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final UserRepository userRepository;

    @Override
    public PaymentDetails createPaymentDetails(PaymentDetailsDto paymentDetailsDto, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .accountNumber(paymentDetailsDto.getAccountNumber())
                .bankName(paymentDetailsDto.getBankName())
                .ifscCode(paymentDetailsDto.getIfscCode())
                .accountHolderName(paymentDetailsDto.getAccountHolderName())
                .user(optionalUser.get())
                .build();
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getPaymentDetailsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        Optional<PaymentDetails> optionalPaymentDetails = paymentDetailsRepository.findByUser(optionalUser.get());
        if (optionalPaymentDetails.isEmpty()) {
            throw new UserInfoException("Payment details not found");
        }
        return optionalPaymentDetails.get();
    }
}
