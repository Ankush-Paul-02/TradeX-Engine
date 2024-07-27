package com.devmare.tradingbackend.business.service.impl;

import com.devmare.tradingbackend.business.service.CoinService;
import com.devmare.tradingbackend.data.entity.Coin;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.CoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinRepository coinRepository;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Coin> getAllCoins(int pageNumber, int pageSize) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=" + pageSize + "&page=" + pageNumber;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readValue(
                    response.getBody(),
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(List.class, Coin.class)
            );
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }

    @Override
    public String getCoinInfo(String coinId) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode marketData = jsonNode.get("market_data");
            Coin coin = Coin.builder()
                    .id(jsonNode.get("id").asText())
                    .symbol(jsonNode.get("symbol").asText())
                    .name(jsonNode.get("name").asText())
                    .image(jsonNode.get("image").get("large").asText())
                    .currentPrice(marketData.get("current_price").get("usd").asDouble())
                    .marketCap(marketData.get("market_cap").get("usd").asLong())
                    .marketCapRank(marketData.get("market_cap_rank").asInt())
                    .totalVolume(marketData.get("total_volume").get("usd").asLong())
                    .high24h(marketData.get("high_24h").get("usd").asDouble())
                    .low24h(marketData.get("low_24h").get("usd").asDouble())
                    .priceChange24h(marketData.get("price_change_24h_in_currency").asDouble())
                    .marketCapChange24h(marketData.get("market_cap_change_24h_in_currency").get("usd").asLong())
                    .priceChangePercentage24h(marketData.get("price_change_percentage_24h_in_currency").asDouble())
                    .build();
            return response.getBody();
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }

    @Override
    public Coin getCoinById(String coinId) {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty()) {
            throw new UserInfoException("Coin not found");
        }
        return optionalCoin.get();
    }

    @Override
    public String searchCoin(String keyword) {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }

    @Override
    public String getTop50CoinsByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=50&page=1";
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }

    @Override
    public String getTradingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending";
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new UserInfoException("Error while fetching data from CoinGecko API");
        }
    }
}
