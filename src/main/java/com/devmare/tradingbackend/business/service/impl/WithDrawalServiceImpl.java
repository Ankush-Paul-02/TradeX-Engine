package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.WalletService;
import com.devmare.tradingbackend.business.service.WithDrawalService;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.entity.WithDrawal;
import com.devmare.tradingbackend.data.repository.UserRepository;
import com.devmare.tradingbackend.data.repository.WithDrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.devmare.tradingbackend.data.enums.WithDrawalStatus.*;

@Service
@RequiredArgsConstructor
public class WithDrawalServiceImpl implements WithDrawalService {

    private final WithDrawalRepository withDrawalRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;

    @Override
    public WithDrawal requestWithDrawal(Double amount, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        WithDrawal withDrawal = WithDrawal.builder()
                .amount(amount)
                .user(optionalUser.get())
                .status(PENDING)
                .build();
        walletService.depositBalance(userId, -amount);
        return withDrawalRepository.save(withDrawal);
    }

    @Override
    public WithDrawal proceedWithDrawal(Long withDrawalId, boolean isApproved) {
        Optional<WithDrawal> optionalWithDrawal = withDrawalRepository.findById(withDrawalId);
        if (optionalWithDrawal.isEmpty()) {
            throw new RuntimeException("WithDrawal not found");
        }
        WithDrawal withDrawal = optionalWithDrawal.get();
        if (isApproved) {
            withDrawal.setStatus(APPROVED);
        } else {
            withDrawal.setStatus(DECLINED);
            walletService.depositBalance(withDrawal.getUser().getId(), withDrawal.getAmount());
        }
        withDrawal.setWithdrawAt(LocalDateTime.now());
        return withDrawalRepository.save(withDrawal);
    }

    @Override
    public List<WithDrawal> getUsersWithDrawals(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return withDrawalRepository.getAllByUser(optionalUser.get());
    }

    @Override
    public List<WithDrawal> getAllWithDrawalRequest() {
        return withDrawalRepository.findAll();
    }
}
