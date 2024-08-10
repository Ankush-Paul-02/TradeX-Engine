package com.devmare.tradingbackend.business.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private String paymentId;
    private String paymentUrl;
}
