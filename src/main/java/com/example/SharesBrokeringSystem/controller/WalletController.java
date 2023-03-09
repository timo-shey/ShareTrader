package com.example.SharesBrokeringSystem.controller;

import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.service.UserService;
import com.example.SharesBrokeringSystem.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/top-up")
    public String getTopUpPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);
        double currentBalance = user.getWallet();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(currentBalance);
        model.addAttribute("walletBalance", formattedBalance);
        return "top-up";
    }

    @PostMapping("/top-up")
    public String postTopUpPage(@RequestParam("amount") double amount, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);
        double currentBalance = user.getWallet();
        double newBalance = currentBalance + amount;
        user.setWallet(newBalance);
        userService.save(user);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(currentBalance);
        model.addAttribute("walletBalance", formattedBalance);
        redirectAttributes.addFlashAttribute("successMessage", "Successfully topped-up GBP " + amount);
        return "redirect:/home/users/" + userId;
    }
}
