package com.devmare.tradingbackend.business.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderDto {

    private double amount;
    private String paymentOrderStatus;
    private String paymentMethod;
}
