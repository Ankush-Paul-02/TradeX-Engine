package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.AssetService;
import com.devmare.tradingbackend.data.entity.Asset;
import com.devmare.tradingbackend.data.entity.Coin;
import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.AssetRepository;
import com.devmare.tradingbackend.data.repository.CoinRepository;
import com.devmare.tradingbackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    @Override
    public Asset createAsset(Long userId, String coinId, double quantity) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty()) {
            throw new UserInfoException("Coin not found");
        }
        Coin coin = optionalCoin.get();

        Asset asset = Asset.builder()
                .user(user)
                .coin(coin)
                .buyPrice(coin.getCurrentPrice())
                .quantity(quantity)
                .build();
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        if (optionalAsset.isEmpty()) {
            throw new UserInfoException("Asset not found");
        }
        return optionalAsset.get();
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(Long userId, Long assetId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<Asset> optionalAsset = assetRepository.findByUserAndId(user, assetId);
        if (optionalAsset.isEmpty()) {
            throw new UserInfoException("Asset not found");
        }
        return optionalAsset.get();
    }

    @Override
    public List<Asset> getAssetsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        return assetRepository.findByUser(user);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        if (optionalAsset.isEmpty()) {
            throw new UserInfoException("Asset not found");
        }
        Asset asset = optionalAsset.get();
        asset.setQuantity(quantity + asset.getQuantity());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetByUserIdAndCoinId(Long userId, String coinId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserInfoException("User not found");
        }
        User user = optionalUser.get();
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty()) {
            throw new UserInfoException("Coin not found");
        }
        Coin coin = optionalCoin.get();
        Optional<Asset> optionalAsset = assetRepository.findByUserAndCoin(user, coin);
        if (optionalAsset.isEmpty()) {
            throw new UserInfoException("Asset not found");
        }
        return optionalAsset.get();
    }

    @Override
    public void deleteAsset(Long assetId) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        if (optionalAsset.isEmpty()) {
            throw new UserInfoException("Asset not found");
        }
        assetRepository.delete(optionalAsset.get());
    }
}
