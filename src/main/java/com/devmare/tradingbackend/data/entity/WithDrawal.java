package com.devmare.tradingbackend.data.entity;

import com.devmare.tradingbackend.data.enums.WithDrawalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "withdrawals")
public class WithDrawal extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private WithDrawalStatus status;

    private double amount;

    @ManyToOne
    private User user;

    private LocalDateTime withdrawAt = LocalDateTime.now();
}
