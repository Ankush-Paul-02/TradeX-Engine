package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.TwoFactorOtp;
import com.devmare.tradingbackend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, Long> {

    Optional<TwoFactorOtp> findByUser(User user);
}
