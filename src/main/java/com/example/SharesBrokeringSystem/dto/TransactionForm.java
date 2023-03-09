package com.example.SharesBrokeringSystem.dto;

import com.example.SharesBrokeringSystem.model.User;

import java.time.LocalDateTime;

public class TransactionForm {
    private Long id;
    private User user;
    private String type;
    private String symbol;
    private String currency;
    private int quantity;
    private Double price;
    private Double priceGBP;
    private Double total;
    private Double walletBalance;
    private String datetime;

    public TransactionForm(Long id, User user, String type, String symbol, String currency, int quantity, Double price, Double priceGBP, Double total, Double walletBalance, String datetime) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.symbol = symbol;
        this.currency = currency;
        this.quantity = quantity;
        this.price = price;
        this.priceGBP = priceGBP;
        this.total = total;
        this.walletBalance = walletBalance;
        this.datetime = datetime;
    }

    public Double getPriceGBP() {
        return priceGBP;
    }

    public void setPriceGBP(Double priceGBP) {
        this.priceGBP = priceGBP;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
