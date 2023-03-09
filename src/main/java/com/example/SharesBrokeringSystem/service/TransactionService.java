package com.example.SharesBrokeringSystem.service;

import com.example.SharesBrokeringSystem.model.Transaction;
import com.example.SharesBrokeringSystem.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    List<Transaction> getAllTransactions();
    List<Transaction> getUserTransaction(User user);
    void saveTransaction(Transaction transaction);
}
