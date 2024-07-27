package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.Order;
import com.devmare.tradingbackend.data.entity.OrderItem;
import com.devmare.tradingbackend.data.enums.OrderType;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrdersByUserId(String orderType, String assetSymbol);

    Order processOrder(String coinId, OrderType orderType, double quantity);
}
