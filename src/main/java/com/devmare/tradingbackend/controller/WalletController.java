package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.service.WalletService;
import com.devmare.tradingbackend.data.entity.WalletTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<DefaultResponse> getUserWallet(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                walletService.getUserWallet(userId)
                        ),
                        "User wallet fetched successfully!"
                )
        );
    }

    @PutMapping("/deposit")
    public ResponseEntity<DefaultResponse> walletToWalletTransfer(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestBody WalletTransaction walletTransaction
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                walletService.walletToWalletTransfer(
                                        senderId,
                                        receiverId,
                                        walletTransaction.getAmount()
                                )
                        ),
                        "Wallet to wallet transfer completed successfully!"
                )
        );
    }

    @PutMapping("/pay-order-payment")
    public ResponseEntity<DefaultResponse> payOrderPayment(
            @RequestParam Long orderId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                walletService.payOrderPayment(
                                        orderId,
                                        userId
                                )
                        ),
                        "Order payment completed successfully!"
                )
        );
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<DefaultResponse> findWalletById(
            @PathVariable Long walletId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                walletService.findWalletById(walletId)
                        ),
                        "Wallet fetched successfully!"
                )
        );
    }
}
