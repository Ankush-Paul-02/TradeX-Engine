package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.Coin;
import com.devmare.tradingbackend.data.entity.WatchList;

public interface WatchListService {

    WatchList findWatchListByUser(Long userId);

    WatchList createWatchList(Long userId);

    WatchList findWatchListById(Long watchListId);

    Coin addCoinToWatchList(Long watchListId, String coinId);
}
