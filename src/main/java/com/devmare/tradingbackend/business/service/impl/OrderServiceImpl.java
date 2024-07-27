package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.AssetService;
import com.devmare.tradingbackend.business.service.CoinService;
import com.devmare.tradingbackend.business.service.OrderService;
import com.devmare.tradingbackend.business.service.WalletService;
import com.devmare.tradingbackend.data.entity.*;
import com.devmare.tradingbackend.data.enums.OrderStatus;
import com.devmare.tradingbackend.data.enums.OrderType;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.AssetRepository;
import com.devmare.tradingbackend.data.repository.OrderItemRepository;
import com.devmare.tradingbackend.data.repository.OrderRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.security.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final OrderItemRepository orderItemRepository;
    private final CoinService coinService;
    private final AuthenticationService authenticationService;
    private final AssetService assetService;

    @Override
    public Order createOrder(
            Long userId,
            OrderItem orderItem,
            OrderType orderType
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();

        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = Order.builder()
                .user(user)
                .orderItem(orderItem)
                .orderType(orderType)
                .price(BigDecimal.valueOf(price))
                .orderAt(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new UserInfoException("Order not found");
        }
        User user = authenticationService.findAuthenticatedUser();
        if (!optionalOrder.get().getUser().getId().equals(user.getId())) {
            throw new UserInfoException("Requested order does not belong to the user");
        }
        return optionalOrder.get();
    }

    @Override
    public List<Order> getAllOrdersByUserId(String orderType, String assetSymbol) {
        User user = authenticationService.findAuthenticatedUser();
        return orderRepository.findAllByUserAndOrderType(user, OrderType.valueOf(orderType));
    }

    private OrderItem createOrderItem(
            Coin coin,
            double quantity,
            double buyPrice,
            double sellPrice
    ) {
        OrderItem orderItem = OrderItem.builder()
                .coin(coin)
                .quantity(quantity)
                .buyPrice(buyPrice)
                .sellPrice(sellPrice)
                .build();
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(
            Coin coin,
            double quantity,
            User user
    ) {
        if (quantity <= 0) {
            throw new UserInfoException("Quantity must be greater than 0");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user.getId(), orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(
                order.getId(),
                user.getId()
        );
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        Asset asset = assetService.getAssetByUserIdAndCoinId(
                order.getUser().getId(),
                order.getOrderItem().getCoin().getId()
        );
        if (asset == null) {
            assetService.createAsset(
                    order.getUser().getId(),
                    order.getOrderItem().getCoin().getId(),
                    order.getOrderItem().getQuantity()
            );
        } else {
            assetService.updateAsset(
                    asset.getId(),
                    order.getOrderItem().getQuantity()
            );
        }
        return savedOrder;
    }

    @Transactional
    public Order sellAsset(
            Coin coin,
            double quantity,
            User user
    ) {
        if (quantity <= 0) {
            throw new UserInfoException("Quantity must be greater than 0");
        }
        Asset currentAsset = assetService.getAssetByUserIdAndCoinId(
                user.getId(),
                coin.getId()
        );

        if (currentAsset != null) {
            double sellPrice = coin.getCurrentPrice();
            double buyPrice = currentAsset.getBuyPrice();

            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = createOrder(user.getId(), orderItem, OrderType.SELL);

            orderItem.setOrder(order);

            if (currentAsset.getQuantity() >= quantity) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(
                        order.getId(),
                        user.getId()
                );
                Asset updatedAsset = assetService.updateAsset(
                        currentAsset.getId(),
                        -quantity
                );

                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }
            throw new UserInfoException("Not enough asset to sell");
        }
        throw new UserInfoException("Asset not found");
    }

    @Override
    @Transactional
    public Order processOrder(
            String coinId,
            OrderType orderType,
            double quantity
    ) {
        Coin coin = coinService.getCoinById(coinId);
        User user = authenticationService.findAuthenticatedUser();
        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin, quantity, user);
        }
        throw new UserInfoException("Invalid order type");
    }
}
