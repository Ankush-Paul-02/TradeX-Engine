package com.devmare.tradingbackend.data.repository;

import com.devmare.tradingbackend.data.entity.Order;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserAndOrderType(User user, OrderType orderType);
}
