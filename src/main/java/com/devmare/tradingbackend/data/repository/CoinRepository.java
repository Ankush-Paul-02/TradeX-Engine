package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
