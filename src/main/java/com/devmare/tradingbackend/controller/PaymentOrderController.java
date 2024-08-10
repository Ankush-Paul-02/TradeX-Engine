package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.PaymentOrderDto;
import com.devmare.tradingbackend.business.service.PaymentOrderService;
import com.devmare.tradingbackend.data.entity.PaymentOrder;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PostMapping("/razorpay/user/{userId}")
    public ResponseEntity<DefaultResponse> createPaymentOrder(
            @PathVariable Long userId,
            @RequestBody PaymentOrderDto paymentOrderDto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentOrder",
                                paymentOrderService.createPaymentOrder(paymentOrderDto, userId)
                        ),
                        "Payment order created successfully"
                )
        );
    }

    @GetMapping("/payment-order/{paymentOrderId}")
    public ResponseEntity<DefaultResponse> getPaymentOrderById(
            @PathVariable Long paymentOrderId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentOrder",
                                paymentOrderService.getPaymentOrderById(paymentOrderId)
                        ),
                        "Payment order fetched successfully"
                )
        );
    }

    @PostMapping("/razorpay/process-payment")
    public ResponseEntity<DefaultResponse> processRazorpayPayment(
            @RequestParam String paymentId,
            @RequestBody PaymentOrder paymentOrder
    ) throws RazorpayException {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentResponse",
                                paymentOrderService.processPaymentOrder(paymentOrder, paymentId)
                        ),
                        "Payment processed successfully"
                )
        );
    }

    @PostMapping("/create-rzp-payment/user/{userId}")
    public ResponseEntity<DefaultResponse> createRazorpayPayment(
            @PathVariable Long userId,
            @RequestParam double amount
    ) throws RazorpayException {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentResponse",
                                paymentOrderService.createRazorpayPayment(userId, amount)
                        ),
                        "Razorpay payment created successfully"
                )
        );
    }
}
