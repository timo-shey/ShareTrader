package com.example.SharesBrokeringSystem.service.impl;

import com.example.SharesBrokeringSystem.enums.TransactionType;
import com.example.SharesBrokeringSystem.exeption.UserNotFoundException;
import com.example.SharesBrokeringSystem.model.Transaction;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.repository.TransactionRepository;
import com.example.SharesBrokeringSystem.repository.UserRepository;
import com.example.SharesBrokeringSystem.service.WalletService;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void topUpBalance(Long userId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        double newBalance = user.getWallet() + amount;
        user.setWallet(newBalance);

        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTotal(amount);
        transaction.setCurrency("GBP");
        transaction.setType(String.valueOf(TransactionType.TOP_UP));
        transaction.setBalance(newBalance);
        transactionRepository.save(transaction);
    }
}
