package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.WatchListService;
import com.devmare.tradingbackend.data.entity.Coin;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.WatchList;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.CoinRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.data.repository.WatchListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    @Override
    public WatchList findWatchListByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<WatchList> optionalWatchList = watchListRepository.findByUser(user);
        if (optionalWatchList.isEmpty()) {
            throw new UserInfoException("WatchList not found");
        }
        return optionalWatchList.get();
    }

    @Override
    public WatchList createWatchList(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        WatchList watchList = WatchList.builder().user(user).build();
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findWatchListById(Long watchListId) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isEmpty()) {
            throw new UserInfoException("WatchList not found");
        }
        return optionalWatchList.get();
    }

    @Override
    public Coin addCoinToWatchList(Long watchListId, String coinId) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isEmpty()) {
            throw new UserInfoException("WatchList not found");
        }
        WatchList watchList = optionalWatchList.get();
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty()) {
            throw new UserInfoException("Coin not found");
        }
        Coin coin = optionalCoin.get();
        if (watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        } else {
            watchList.getCoins().add(coin);
        }
        watchListRepository.save(watchList);
        return coin;
    }
}
