package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/watchlist")
public class WatchListController {

    private final WatchListService watchListService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultResponse> findWatchListByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "watchList",
                                watchListService.findWatchListByUser(userId)
                        ),
                        "Watchlist found successfully"
                )
        );
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<DefaultResponse> createWatchList(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "watchList",
                                watchListService.createWatchList(userId)
                        ),
                        "Watchlist created successfully"
                )
        );
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<DefaultResponse> findWatchListById(
            @PathVariable Long watchListId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "watchList",
                                watchListService.findWatchListById(watchListId)
                        ),
                        "Watchlist found successfully"
                )
        );
    }

    @PostMapping("/{watchListId}/coin/{coinId}")
    public ResponseEntity<DefaultResponse> addCoinToWatchList(
            @PathVariable Long watchListId,
            @PathVariable String coinId
    ) {
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "coin",
                                watchListService.addCoinToWatchList(watchListId, coinId)
                        ),
                        "Coin added to watchlist successfully"
                )
        );
    }
}
