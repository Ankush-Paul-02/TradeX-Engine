package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.service.WithDrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/withdrawal")
public class WithDrawalController {

    private final WithDrawalService withDrawalService;

    @PostMapping("/{userId}")
    public ResponseEntity<DefaultResponse> requestWithDrawal(
            @RequestParam Double amount,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "withdrawal",
                                withDrawalService.requestWithDrawal(amount, userId)
                        ),
                        "Withdrawal requested successfully"
                )
        );
    }

    @PutMapping("/{withdrawalId}")
    public ResponseEntity<DefaultResponse> proceedWithDrawal(
            @RequestParam boolean isApproved,
            @PathVariable Long withdrawalId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "withdrawal",
                                withDrawalService.proceedWithDrawal(
                                        withdrawalId,
                                        isApproved
                                )
                        ),
                        "Withdrawal proceeded successfully"
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultResponse> getUsersWithDrawals(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "withdrawals",
                                withDrawalService.getUsersWithDrawals(userId)
                        ),
                        "Withdrawals fetched successfully"
                )
        );
    }

    @GetMapping("/all")
    public ResponseEntity<DefaultResponse> getAllWithDrawalRequest() {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "withdrawals",
                                withDrawalService.getAllWithDrawalRequest()
                        ),
                        "Withdrawals fetched successfully"
                )
        );
    }
}
