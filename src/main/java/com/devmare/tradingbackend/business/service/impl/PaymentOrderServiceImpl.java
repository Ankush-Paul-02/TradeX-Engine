package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.dto.PaymentOrderDto;
import com.devmare.tradingbackend.business.dto.PaymentResponseDto;
import com.devmare.tradingbackend.business.service.PaymentOrderService;
import com.devmare.tradingbackend.data.entity.PaymentOrder;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.enums.PaymentMethod;
import com.devmare.tradingbackend.data.enums.PaymentOrderStatus;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.PaymentOrderRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final UserRepository userRepository;

    @Value("${application.razorpay.key}")
    private String razorpayKey;

    @Value("${application.razorpay.secret}")
    private String razorpayKeySecret;

    @Override
    public PaymentOrder createPaymentOrder(PaymentOrderDto paymentOrderDto, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .amount(paymentOrderDto.getAmount())
                .paymentMethod(PaymentMethod.valueOf(paymentOrderDto.getPaymentMethod()))
                .status(PaymentOrderStatus.valueOf(paymentOrderDto.getPaymentOrderStatus()))
                .user(user)
                .build();
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) {
        Optional<PaymentOrder> optionalPaymentOrder = paymentOrderRepository.findById(paymentOrderId);
        if (optionalPaymentOrder.isEmpty()) {
            throw new UserInfoException("Payment order not found");
        }
        return optionalPaymentOrder.get();
    }

    @Override
    public Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpayKeySecret);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                if (payment != null) {
                    Double amount = payment.get("amount");
                    String status = payment.get("status");

                    if ("captured".equals(status) && amount.equals(paymentOrder.getAmount() * 100)) {
                        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                        paymentOrderRepository.save(paymentOrder);
                        return true;
                    }
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }


    @Override
    public PaymentResponseDto createRazorpayPayment(Long userId, double amount) throws RazorpayException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpayKeySecret);

            JSONObject paymentRequest = new JSONObject();
            paymentRequest.put("amount", amount * 100);
            paymentRequest.put("currency", "INR");

            JSONObject notes = new JSONObject();
            notes.put("userId", user.getId());
            notes.put("name", user.getFullname());
            notes.put("email", user.getEmail());
            paymentRequest.put("notes", notes);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentRequest.put("notify", notify);

            paymentRequest.put("reminder_enable", true);

            paymentRequest.put("callback_url", "https://example.com/callback");
            paymentRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentRequest);
            String paymentLinkId = paymentLink.get("id");
            String shortUrl = paymentLink.get("short_url");

            return PaymentResponseDto.builder()
                    .paymentId(paymentLinkId)
                    .paymentUrl(shortUrl)
                    .build();
        } catch (RazorpayException e) {
            throw new UserInfoException(e.getMessage());
        }
    }

    @Override
    public PaymentResponseDto createStripePayment(Long userId, double amount, Long orderId) {
        // TODO: Implement this method
        return null;
    }
}
