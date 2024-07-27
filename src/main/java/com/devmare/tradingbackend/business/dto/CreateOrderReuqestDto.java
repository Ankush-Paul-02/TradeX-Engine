package com.devmare.tradingbackend.business.dto;

import com.devmare.tradingbackend.data.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderReuqestDto {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
