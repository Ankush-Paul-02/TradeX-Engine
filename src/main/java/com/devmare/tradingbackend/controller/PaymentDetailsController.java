package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.PaymentDetailsDto;
import com.devmare.tradingbackend.business.service.PaymentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-details")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;

    @PostMapping("/create/user/{userId}")
    public ResponseEntity<DefaultResponse> createPaymentDetails(
            @RequestBody PaymentDetailsDto paymentDetailsDto,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentDetails",
                                paymentDetailsService.createPaymentDetails(paymentDetailsDto, userId)
                        ),
                        "Payment details created successfully"
                )
        );
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<DefaultResponse> getPaymentDetails(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "paymentDetails",
                                paymentDetailsService.getPaymentDetailsByUserId(userId)
                        ),
                        "Payment details fetched successfully"
                )
        );
    }
}
