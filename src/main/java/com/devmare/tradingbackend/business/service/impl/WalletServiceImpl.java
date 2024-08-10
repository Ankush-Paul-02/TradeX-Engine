package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.PaymentOrderService;
import com.devmare.tradingbackend.business.service.WalletService;
import com.devmare.tradingbackend.data.entity.Order;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.Wallet;
import com.devmare.tradingbackend.data.enums.OrderType;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.OrderRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.data.repository.WalletRepository;
import com.devmare.tradingbackend.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentOrderService paymentOrderService;
    private final AuthenticationService authenticationService;

    @Override
    public Wallet getUserWallet(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<Wallet> optionalWallet = walletRepository.findByUser(user);
        if (optionalWallet.isEmpty()) {
            throw new UserInfoException("Wallet not found");
        }
        return optionalWallet.get();
    }

    @Override
    public void depositBalance(Long walletId, Double amount) {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            throw new UserInfoException("Wallet not found");
        }
        Wallet wallet = optionalWallet.get();
        BigDecimal balance = wallet.getBalance();
        wallet.setBalance(balance.add(BigDecimal.valueOf(amount)));
        walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long walletId) {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            throw new UserInfoException("Wallet not found");
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet walletToWalletTransfer(Long senderId, Long receiverId, Double amount) {
        Optional<User> optionalSender = userRepository.findById(senderId);
        if (optionalSender.isEmpty()) {
            throw new UserInfoException("Sender not found");
        }
        User sender = optionalSender.get();
        Optional<User> optionalReceiver = userRepository.findById(receiverId);
        if (optionalReceiver.isEmpty()) {
            throw new UserInfoException("Receiver not found");
        }
        User receiver = optionalReceiver.get();
        Optional<Wallet> optionalSenderWallet = walletRepository.findByUser(sender);
        if (optionalSenderWallet.isEmpty()) {
            throw new UserInfoException("Sender wallet not found");
        }
        Wallet senderWallet = optionalSenderWallet.get();
        Optional<Wallet> optionalReceiverWallet = walletRepository.findByUser(receiver);
        if (optionalReceiverWallet.isEmpty()) {
            throw new UserInfoException("Receiver wallet not found");
        }
        Wallet receiverWallet = optionalReceiverWallet.get();

        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new UserInfoException("Insufficient balance");
        }
        BigDecimal senderBalance = senderWallet.getBalance();
        senderWallet.setBalance(senderBalance.subtract(BigDecimal.valueOf(amount)));
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiverWallet.getBalance();
        receiverWallet.setBalance(receiverBalance.add(BigDecimal.valueOf(amount)));
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Long orderId, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new UserInfoException("Order not found");
        }
        Order order = optionalOrder.get();

        Optional<Wallet> optionalWallet = walletRepository.findByUser(user);
        if (optionalWallet.isEmpty()) {
            throw new UserInfoException("Wallet not found");
        }
        Wallet wallet = getWallet(optionalWallet.get(), order);
        walletRepository.save(wallet);
        return wallet;
    }

    private static Wallet getWallet(Wallet wallet, Order order) {
        if (order.getOrderType().equals(OrderType.BUY)) {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new UserInfoException("Insufficient balance");
            }
            wallet.setBalance(newBalance);
        } else if (order.getOrderType().equals(OrderType.SELL)) {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        } else {
            throw new UserInfoException("Invalid order type");
        }
        return wallet;
    }
}
