package com.example.SharesBrokeringSystem.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public interface CurrencyConversionService {
    String convertCurrencyAPI(String fromCurrency, String toCurrency, Double amount) throws IOException;
    double convertCurrency(String fromCurrency, String toCurrency, Double amount) throws IOException;
    double exchangeRate(String fromCurrency, String toCurrency, Double amount) throws IOException;
}
