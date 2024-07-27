package com.devmare.tradingbackend.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    private Double quantity;

    @ManyToOne
    private Coin coin;

    private Double buyPrice;

    private Double sellPrice;

    @OneToOne
    @JsonIgnore
    private Order order;
}
