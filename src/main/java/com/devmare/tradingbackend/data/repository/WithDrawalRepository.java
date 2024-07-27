package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.WithDrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithDrawalRepository extends JpaRepository<WithDrawal, Long> {

    List<WithDrawal> getAllByUser(User user);
}
