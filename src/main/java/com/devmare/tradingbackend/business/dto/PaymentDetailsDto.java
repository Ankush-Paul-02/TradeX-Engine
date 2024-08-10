package com.devmare.tradingbackend.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailsDto {

    private String accountNumber;
    private String accountHolderName;
    private String bankName;
    private String ifscCode;
}
