package com.devmare.tradingbackend.data.entity;

import com.devmare.tradingbackend.data.enums.WalletTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet_transactions")
public class WalletTransaction extends BaseEntity {

    @ManyToOne
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private WalletTransactionType type;

    private LocalDate date;

    private String transferId;

    private String purpose;

    private Double amount;
}
