package com.example.SharesBrokeringSystem.repository;

import com.example.SharesBrokeringSystem.model.Transaction;
import com.example.SharesBrokeringSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
