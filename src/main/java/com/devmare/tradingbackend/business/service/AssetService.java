package com.devmare.tradingbackend.business.service;

import com.devmare.tradingbackend.data.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset createAsset(Long userId, String coinId, double quantity);

    Asset getAssetById(Long assetId);

    Asset getAssetByUserIdAndAssetId(Long userId, Long assetId);

    List<Asset> getAssetsByUserId(Long userId);

    Asset updateAsset(Long assetId, double quantity);

    Asset getAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);
}
