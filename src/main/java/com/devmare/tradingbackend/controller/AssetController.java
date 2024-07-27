package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.dto.AssetRequestDto;
import com.devmare.tradingbackend.business.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/asset")
public class AssetController {

    private final AssetService assetService;

    @PostMapping("/create")
    public ResponseEntity<DefaultResponse> createAsset(
            @RequestBody AssetRequestDto assetRequestDto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "asset",
                                assetService.createAsset(
                                        assetRequestDto.getUserId(),
                                        assetRequestDto.getCoinId(),
                                        assetRequestDto.getQuantity()
                                )
                        ),
                        "Asset created successfully"
                )
        );
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<DefaultResponse> getAssetById(
            @PathVariable Long assetId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "asset",
                                assetService.getAssetById(assetId)
                        ),
                        "Asset retrieved successfully"
                )
        );
    }

    @GetMapping("{assetId}/user/{userId}/")
    public ResponseEntity<DefaultResponse> getAssetByUserIdAndAssetId(
            @PathVariable Long assetId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "asset",
                                assetService.getAssetByUserIdAndAssetId(
                                        userId,
                                        assetId
                                )
                        ),
                        "Asset retrieved successfully"
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultResponse> getAssetsByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "assets",
                                assetService.getAssetsByUserId(userId)
                        ),
                        "Assets retrieved successfully"
                )
        );
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<DefaultResponse> updateAsset(
            @PathVariable Long assetId,
            @RequestParam double quantity
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "asset",
                                assetService.updateAsset(
                                        assetId,
                                        quantity
                                )
                        ),
                        "Asset updated successfully"
                )
        );
    }

    @GetMapping("/user/{userId}/coin/{coinId}")
    public ResponseEntity<DefaultResponse> getAssetByUserIdAndCoinId(
            @PathVariable Long userId,
            @PathVariable String coinId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "asset",
                                assetService.getAssetByUserIdAndCoinId(
                                        userId,
                                        coinId
                                )
                        ),
                        "Asset retrieved successfully"
                )
        );
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<DefaultResponse> deleteAsset(
            @PathVariable Long assetId
    ) {
        assetService.deleteAsset(assetId);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        null,
                        "Asset deleted successfully"
                )
        );
    }
}
