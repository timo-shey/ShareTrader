package com.example.SharesBrokeringSystem.model;

import jakarta.persistence.*;

import java.util.Date;
@Table(name = "Transactions")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long userId;
    private Long shareId;
    private Date transactionDate;
    private int numShares;
    private double price;
    private String transactionType;

    public Transaction() {
    }

    public Transaction(Long transactionId, Long userId, Long shareId, Date transactionDate, int numShares, double price, String transactionType) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.shareId = shareId;
        this.transactionDate = transactionDate;
        this.numShares = numShares;
        this.price = price;
        this.transactionType = transactionType;
    }

    public Transaction(Long transactionId, Date transactionDate, int numShares, double price, String transactionType) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.numShares = numShares;
        this.price = price;
        this.transactionType = transactionType;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getNumShares() {
        return numShares;
    }

    public void setNumShares(int numShares) {
        this.numShares = numShares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", shareId=" + shareId +
                ", transactionDate=" + transactionDate +
                ", numShares=" + numShares +
                ", price=" + price +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
