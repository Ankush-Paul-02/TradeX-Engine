package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}
