package com.devmare.tradingbackend.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequestDto {

    private Long userId;
    private String coinId;
    private double quantity;
}
