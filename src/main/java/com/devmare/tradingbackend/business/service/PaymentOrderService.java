package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.business.dto.PaymentOrderDto;
import com.devmare.tradingbackend.business.dto.PaymentResponseDto;
import com.devmare.tradingbackend.data.entity.PaymentOrder;
import com.razorpay.RazorpayException;

public interface PaymentOrderService {

    PaymentOrder createPaymentOrder(PaymentOrderDto paymentOrderDto, Long userId);

    PaymentOrder getPaymentOrderById(Long paymentOrderId);

    Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponseDto createRazorpayPayment(
            Long userId,
            double amount
    ) throws RazorpayException;

    PaymentResponseDto createStripePayment(
            Long userId,
            double amount,
            Long orderId
    );
}
