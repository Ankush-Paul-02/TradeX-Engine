package com.devmare.tradingbackend.data.entity;

import com.devmare.tradingbackend.data.enums.VerificatonType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorAuthentication {

    private Boolean isEnabled = false;

    @Enumerated(EnumType.STRING)
    private VerificatonType sendTo;
}
