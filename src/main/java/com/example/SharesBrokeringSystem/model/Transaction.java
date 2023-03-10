package com.example.SharesBrokeringSystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "transaction")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    private String type;

    @Column(name = "symbol")
    private String symbol;
    @Column(name = "currency")
    private String currency;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "priceGBP")
    private Double priceGBP;

    @Column(name = "total")
    private Double total;
    @Column(name = "balance")
    private Double balance;

    @Column(name = "datetime")
    private String date;

    public Transaction() {}

    public Transaction(Long id, User user, String type, String symbol, String currency, int quantity, Double price, Double priceGBP, Double total, Double balance, String date) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.symbol = symbol;
        this.currency = currency;
        this.quantity = quantity;
        this.price = price;
        this.priceGBP = priceGBP;
        this.total = total;
        this.balance = balance;
        this.date = date;
    }

    public Double getPriceGBP() {
        return priceGBP;
    }

    public void setPriceGBP(Double priceGBP) {
        this.priceGBP = priceGBP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

}
