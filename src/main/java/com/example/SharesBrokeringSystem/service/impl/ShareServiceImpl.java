package com.example.SharesBrokeringSystem.service.impl;

import com.example.SharesBrokeringSystem.enums.TransactionType;
import com.example.SharesBrokeringSystem.exeption.ShareNotFoundException;
import com.example.SharesBrokeringSystem.exeption.SharesBrokeringException;
import com.example.SharesBrokeringSystem.exeption.UserNotFoundException;
import com.example.SharesBrokeringSystem.model.Portfolio;
import com.example.SharesBrokeringSystem.model.Share;
import com.example.SharesBrokeringSystem.model.Transaction;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.repository.PortfolioRepository;
import com.example.SharesBrokeringSystem.repository.ShareRepository;
import com.example.SharesBrokeringSystem.repository.TransactionRepository;
import com.example.SharesBrokeringSystem.repository.UserRepository;
import com.example.SharesBrokeringSystem.service.CurrencyConversionService;
import com.example.SharesBrokeringSystem.service.ShareService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.finnhub.api.models.CompanyNews;
import io.finnhub.api.models.CompanyProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShareServiceImpl implements ShareService {

    @Value("${finnhub.api.url}")
    private String baseUrl;
    @Value("${finnhub.api.token}")
    private String token;
    private final ShareRepository shareRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyConversionService currencyConversionService;
    private final RestTemplate restTemplate;

    public ShareServiceImpl(ShareRepository shareRepository, UserRepository userRepository, PortfolioRepository portfolioRepository, TransactionRepository transactionRepository, CurrencyConversionService currencyConversionService, RestTemplateBuilder restTemplateBuilder) {
        this.shareRepository = shareRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
        this.currencyConversionService = currencyConversionService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public boolean purchaseShare(String symbol, int quantity, User user) throws IOException {
        double price = getStockPrice(symbol);
        String currency = getStockCurrency(symbol);
        double priceGBP = currencyConversionService.convertCurrency(currency, "GBP", price);
        double total = priceGBP * quantity;

        Optional<User> activeUser = Optional.ofNullable(userRepository.findById(user.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ));

        if (activeUser.get().getWallet() < total) {
            return false;
        }

        activeUser.get().setWallet(activeUser.get().getWallet() - total);
        Portfolio portfolio = activeUser.get().getPortfolio();
        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setUser(activeUser.get());
        }

        Map<String, Integer> holdings = portfolio.getHoldings();
        if (holdings.containsKey(symbol)) {
            int currentQuantity = holdings.get(symbol);
            holdings.put(symbol, currentQuantity + quantity);
        } else {
            holdings.put(symbol, quantity);
        }
        portfolio.setHoldings(holdings);
        portfolioRepository.save(portfolio);
        userRepository.save(activeUser.get());
        Transaction transaction = new Transaction();
        transaction.setUser(activeUser.get());
        transaction.setSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setType(String.valueOf(TransactionType.BUY));
        transaction.setPrice(getStockPrice(symbol));
        transaction.setPriceGBP(priceGBP);
        transaction.setTotal(total);
        transaction.setDate(String.valueOf(LocalDate.now()));
        transaction.setCurrency(currency);
        transaction.setBalance(activeUser.get().getWallet());
        transactionRepository.save(transaction);

        return true;
    }

    @Override
    public boolean sellShare(String symbol, int quantity, User user) throws IOException {
        double price = getStockPrice(symbol);
        String currency = getStockCurrency(symbol);
        double priceGBP = currencyConversionService.convertCurrency(currency, "GBP", price);
        double total = priceGBP * quantity;

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(user.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ));

        Portfolio portfolio = optionalUser.get().getPortfolio();
        if (portfolio == null) {
            throw new ShareNotFoundException("Share not found in portfolio");
        }

        Map<String, Integer> holdings = portfolio.getHoldings();

        if (!holdings.containsKey(symbol)) {
            throw new ShareNotFoundException("Share not found in portfolio");
        }

        int currentQuantity = holdings.get(symbol);
        if (currentQuantity < quantity) {
            throw new SharesBrokeringException("Not enough shares to sell");
        }

        int remainingQuantity = currentQuantity - quantity;
        if (remainingQuantity == 0) {
            holdings.remove(symbol);
        } else {
            holdings.put(symbol, remainingQuantity);
        }

        portfolio.setHoldings(holdings);
        portfolioRepository.save(portfolio);

        optionalUser.get().setWallet(optionalUser.get().getWallet() + total);
        userRepository.save(optionalUser.get());
        Transaction transaction = new Transaction();
        transaction.setUser(optionalUser.get());
        transaction.setSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setType(String.valueOf(TransactionType.SELL));
        transaction.setPrice(price);
        transaction.setPriceGBP(priceGBP);
        transaction.setTotal(total);
        transaction.setDate(String.valueOf(LocalDate.now()));
        transaction.setCurrency(currency);
        transaction.setBalance(optionalUser.get().getWallet());
        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public CompanyProfile getCompanyProfile(String symbol) {
        String url = baseUrl + "/stock/profile2";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("token", token)
                .queryParam("symbol", symbol);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), CompanyProfile.class);
    }

    @Override
    public List<CompanyNews> getCompanyNews(String symbol, LocalDate from, LocalDate to) {
        String url = baseUrl + "/company-news";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("token", token)
                .queryParam("symbol", symbol)
                .queryParam("from", from)
                .queryParam("to", to);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CompanyNews>>(){}.getType();
        return gson.fromJson(response.getBody(), listType);
    }

    @Override
    public Double getStockPrice(String symbol) {
        String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", symbol, token);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("c").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get stock price for symbol " + symbol, e);
        }
    }

    @Override
    public String getStockCurrency(String symbol) throws JsonProcessingException {
        String url = "https://finnhub.io/api/v1" + "/stock/profile2?symbol=" + symbol + "&token=" + token;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("currency").asText();
    }

    @Override
    public String getCompanyName(String symbol) throws JsonProcessingException {
        String url = "https://finnhub.io/api/v1" + "/stock/profile2?symbol=" + symbol + "&token=" + token;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("name").asText();
    }

}
