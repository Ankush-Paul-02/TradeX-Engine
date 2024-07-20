package com.devmare.tradingbackend.security;

import com.devmare.tradingbackend.data.entity.RefreshToken;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.RefreshTokenRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refresTokenRepository;

    @Value("${application.refresh.expiration}")
    public Long REFRSH_TOKEN_EXPIRATION;

    public RefreshToken createRefreshToken(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            refreshToken = RefreshToken
                    .builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(REFRSH_TOKEN_EXPIRATION))
                    .user(user)
                    .build();
            refresTokenRepository.save(refreshToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refresTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpirationTime().compareTo(Instant.now()) < 0) {
            refresTokenRepository.delete(token);
            throw new RuntimeException(token.getRefreshToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
