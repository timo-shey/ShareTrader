package com.example.SharesBrokeringSystem.service;

import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    void topUpBalance(Long userId, double amount);
}
