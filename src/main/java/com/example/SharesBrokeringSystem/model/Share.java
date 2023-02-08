package com.example.SharesBrokeringSystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Table(name = "Shares")
@Entity
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;
    private String companyName;
    private String companySymbol;
    private int numShares;
    private String shareCurrency;
    private double sharePrice;
    private LocalDateTime lastUpdate;

    public Share() {
    }

    public Share(Long shareId, String companyName, String companySymbol, int numShares, String shareCurrency, double sharePrice, LocalDateTime lastUpdate) {
        this.shareId = shareId;
        this.companyName = companyName;
        this.companySymbol = companySymbol;
        this.numShares = numShares;
        this.shareCurrency = shareCurrency;
        this.sharePrice = sharePrice;
        this.lastUpdate = lastUpdate;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    public int getNumShares() {
        return numShares;
    }

    public void setNumShares(int numShares) {
        this.numShares = numShares;
    }

    public String getShareCurrency() {
        return shareCurrency;
    }

    public void setShareCurrency(String shareCurrency) {
        this.shareCurrency = shareCurrency;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Share{" +
                "shareId=" + shareId +
                ", companyName='" + companyName + '\'' +
                ", companySymbol='" + companySymbol + '\'' +
                ", numShares=" + numShares +
                ", shareCurrency='" + shareCurrency + '\'' +
                ", sharePrice=" + sharePrice +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
