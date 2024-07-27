package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.Asset;
import com.devmare.tradingbackend.data.entity.Coin;
import com.devmare.tradingbackend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByUser(User user);

    Optional<Asset> findByUserAndCoin(User user, Coin coin);

    Optional<Asset> findByUserAndId(User user, Long id);
}
