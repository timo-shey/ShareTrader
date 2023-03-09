package com.example.SharesBrokeringSystem.controller;

import com.example.SharesBrokeringSystem.dto.TransactionForm;
import com.example.SharesBrokeringSystem.model.Transaction;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.service.TransactionService;
import com.example.SharesBrokeringSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    public String showTransactions(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);
        List<Transaction> transactions = transactionService.getUserTransaction(user);
        model.addAttribute("transactions", transactions);
        double currentBalance = user.getWallet();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(currentBalance);
        model.addAttribute("walletBalance", formattedBalance);
        return "transactions";
    }

    @PostMapping("/transactions")
    public String addTransaction(@ModelAttribute("transactionForm") TransactionForm transactionForm, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);
        double currentBalance = user.getWallet();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(currentBalance);
        model.addAttribute("walletBalance", formattedBalance);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction added successfully");
        return "redirect:/transactions";
    }
}
