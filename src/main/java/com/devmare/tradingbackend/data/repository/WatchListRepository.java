package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    Optional<WatchList> findByUser(User user);
}
