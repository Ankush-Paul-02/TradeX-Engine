package com.devmare.tradingbackend.controller;

import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.business.service.CoinService;
import com.devmare.tradingbackend.data.entity.Coin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coin")
public class CoinController {

    private final CoinService coinService;
    private final ObjectMapper objectMapper;

    @GetMapping("/list")
    public ResponseEntity<DefaultResponse> getCoinList(
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize
    ) {
        List<Coin> coins = coinService.getAllCoins(pageNumber, pageSize);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                coins
                        ),
                        "Coin list fetched successfully!"
                )
        );
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<DefaultResponse> getMaketChart(
            @PathVariable String coinId,
            @RequestParam("days") int days
    ) throws JsonProcessingException {
        String chartData = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(chartData);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                jsonNode
                        ),
                        "Market chart fetched successfully!"
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<DefaultResponse> searchCoin(
            @RequestParam("keyword") String keyword
    ) throws JsonProcessingException {
        String coin = coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                jsonNode
                        ),
                        "Search result fetched successfully!"
                )
        );
    }

    @GetMapping("/top50")
    public ResponseEntity<DefaultResponse> getTop50Coins() throws JsonProcessingException {
        String coins = coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                jsonNode
                        ),
                        "Top 50 coints fetched successfully!"
                )
        );
    }

    @GetMapping("/trending")
    public ResponseEntity<DefaultResponse> getTrendingCoins() throws JsonProcessingException {
        String coins = coinService.getTradingCoins();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                jsonNode
                        ),
                        "Trending coints fetched successfully!"
                )
        );
    }

    @GetMapping("/info/{coinId}")
    public ResponseEntity<DefaultResponse> getCoinInfo(
            @PathVariable String coinId
    ) throws JsonProcessingException {
        String coins = coinService.getCoinInfo(coinId);
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(
                new DefaultResponse(
                        SUCCESS,
                        Map.of(
                                "data",
                                jsonNode
                        ),
                        "Coin info fetched successfully!"
                )
        );
    }
}
