package com.example.SharesBrokeringSystem.service;

import com.example.SharesBrokeringSystem.dto.Stock;
import com.example.SharesBrokeringSystem.model.Share;
import com.example.SharesBrokeringSystem.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.finnhub.api.models.CompanyNews;
import io.finnhub.api.models.CompanyProfile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ShareService {
    boolean purchaseShare(String symbol, int quantity, User user) throws IOException;
    boolean sellShare(String symbol, int quantity, User user) throws IOException;
    CompanyProfile getCompanyProfile(String symbol) throws IOException;
    List<CompanyNews> getCompanyNews(String symbol, LocalDate from, LocalDate to);
    Double getStockPrice(String symbol);
    String getStockCurrency(String symbol) throws JsonProcessingException;
    String getCompanyName(String symbol) throws JsonProcessingException;
}