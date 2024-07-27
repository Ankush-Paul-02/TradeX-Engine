package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getAllCoins(int pageNumber, int pageSize);

    String getMarketChart(String coinId, int days);

    String getCoinInfo(String coinId);

    Coin getCoinById(String coinId);

    String searchCoin(String keyword);

    String getTop50CoinsByMarketCapRank();

    String getTradingCoins();
}
