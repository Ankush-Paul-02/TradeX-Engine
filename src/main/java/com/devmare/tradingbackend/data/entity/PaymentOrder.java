package com.devmare.tradingbackend.data.entity;

import com.devmare.tradingbackend.data.enums.PaymentMethod;
import com.devmare.tradingbackend.data.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_orders")
public class PaymentOrder extends BaseEntity {

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;
}
