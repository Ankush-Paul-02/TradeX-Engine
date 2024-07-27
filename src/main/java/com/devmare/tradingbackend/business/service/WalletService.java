package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.Wallet;

public interface WalletService {

    Wallet getUserWallet(Long userId);

    void depositBalance(Long walletId, Double amount);

    Wallet findWalletById(Long walletId);

    Wallet walletToWalletTransfer(Long senderId, Long receiverId, Double amount);

    Wallet payOrderPayment(Long orderId, Long userId);
}
