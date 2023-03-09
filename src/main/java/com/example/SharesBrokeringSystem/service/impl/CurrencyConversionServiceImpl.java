package com.example.SharesBrokeringSystem.service.impl;

import com.example.SharesBrokeringSystem.service.CurrencyConversionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    @Value("${currency.api.url}")
    private String currencyApiUrl;

    private final RestTemplate restTemplate;

    public CurrencyConversionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String convertCurrencyAPI(String fromCurrency, String toCurrency, Double amount) throws IOException {
        String convertedAmount = "0.0";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(currencyApiUrl)).newBuilder();
        urlBuilder.addQueryParameter("from", fromCurrency);
        urlBuilder.addQueryParameter("to", toCurrency);
        urlBuilder.addQueryParameter("amount", Double.toString(amount));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String responseBody = response.body().string();
        double exchangeRate = Double.parseDouble(responseBody);
        double changedAmount = amount * exchangeRate;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedAmount = df.format(changedAmount);
        double parsedAmount = Double.parseDouble(formattedAmount.replace(",", ""));
        convertedAmount = toCurrency + " " + df.format(parsedAmount);

        return convertedAmount;
    }

    @Override
    public double convertCurrency(String fromCurrency, String toCurrency, Double amount) throws IOException {
        double convertedAmount = 0.0;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(currencyApiUrl)).newBuilder();
        urlBuilder.addQueryParameter("from", fromCurrency);
        urlBuilder.addQueryParameter("to", toCurrency);
        urlBuilder.addQueryParameter("amount", Double.toString(amount));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String responseBody = response.body().string();
        double exchangeRate = Double.parseDouble(responseBody);
        double changedAmount = amount * exchangeRate;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedAmount = df.format(changedAmount);
        double parsedAmount = Double.parseDouble(formattedAmount.replace(",", ""));
        convertedAmount = Double.parseDouble(df.format(parsedAmount));

        return convertedAmount;
    }

    @Override
    public double exchangeRate(String fromCurrency, String toCurrency, Double amount) throws IOException {
        double convertedAmount = 0.0;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(currencyApiUrl)).newBuilder();
        urlBuilder.addQueryParameter("from", fromCurrency);
        urlBuilder.addQueryParameter("to", toCurrency);
        urlBuilder.addQueryParameter("amount", Double.toString(amount));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String responseBody = response.body().string();
        double exchangeRate = Double.parseDouble(responseBody);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedAmount = df.format(exchangeRate);
        double parsedAmount = Double.parseDouble(formattedAmount.replace(",", ""));
        convertedAmount = Double.parseDouble(df.format(parsedAmount));

        return convertedAmount;
    }


}
