package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.business.dto.PaymentDetailsDto;
import com.devmare.tradingbackend.data.entity.PaymentDetails;

public interface PaymentDetailsService {

    PaymentDetails createPaymentDetails(PaymentDetailsDto paymentDetailsDto, Long userId);

    PaymentDetails getPaymentDetailsByUserId(Long userId);
}
