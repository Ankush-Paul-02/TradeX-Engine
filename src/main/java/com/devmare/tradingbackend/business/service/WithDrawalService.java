package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.WithDrawal;

import java.util.List;

public interface WithDrawalService {

    WithDrawal requestWithDrawal(Double amount, Long userId);

    WithDrawal proceedWithDrawal(Long withDrawalId, boolean isApproved);

    List<WithDrawal> getUsersWithDrawals(Long userId);

    List<WithDrawal> getAllWithDrawalRequest();
}
