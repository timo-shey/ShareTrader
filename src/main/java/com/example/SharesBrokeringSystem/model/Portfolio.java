package com.example.SharesBrokeringSystem.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Table(name = "portfolio")
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ElementCollection
    private Map<String, Integer> holdings;
    public Portfolio() {
        holdings = new HashMap<>();
    }


    public Map<String, Integer> getHoldings() {
        return holdings;
    }

    public void setHoldings(Map<String, Integer> holdings) {
        if (holdings == null) {
            initializeDefaultValues();
        } else {
            this.holdings = holdings;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    public void initializeDefaultValues() {
        if (holdings == null) {
            holdings = new HashMap<>();
            holdings.put("AAPL", 0);
        }
    }
}
