package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.PaymentDetails;
import com.devmare.tradingbackend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    Optional<PaymentDetails> findByUser(User user);
}
