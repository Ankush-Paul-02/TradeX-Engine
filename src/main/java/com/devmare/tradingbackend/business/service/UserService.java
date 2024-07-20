package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.business.dto.UpdatePasswordDto;
import com.devmare.tradingbackend.data.entity.User;

public interface UserService {

    User findAuthenticatedUser();

    User findUserByEmail(String email);

    User findUserById(Long id);

    User enableTwoFactorAuthentication(Long userId, String verificatonType);

    User updatePassword(UpdatePasswordDto updatePasswordDto);
}
