package com.example.SharesBrokeringSystem.controller;

import com.example.SharesBrokeringSystem.dto.Conversion;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.service.CurrencyConversionService;
import com.example.SharesBrokeringSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.DecimalFormat;

@Controller
@RequestMapping("/convert")
public class CurrencyConversionController {
    private final CurrencyConversionService currencyConversionService;
    private final UserService userService;

    public CurrencyConversionController(CurrencyConversionService currencyConversionService, UserService userService) {
        this.currencyConversionService = currencyConversionService;
        this.userService = userService;
    }

    @GetMapping
    public String showConversionForm(Model model, HttpSession session) {
        Object userDetails = session.getAttribute("name");
        if (userDetails == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);
        double walletBalance = user.getWallet();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(walletBalance);
        model.addAttribute("walletBalance", formattedBalance);
        model.addAttribute("conversion", new Conversion());
        return "conversion-form";
    }

    @PostMapping
    public String convertCurrency(@ModelAttribute Conversion conversion, Model model) throws IOException {
        String convertedAmount = currencyConversionService.convertCurrencyAPI(conversion.getFrom(), conversion.getTo(), conversion.getAmount());
        double exchangeRate = currencyConversionService.exchangeRate(conversion.getFrom(), conversion.getTo(), conversion.getAmount());
        model.addAttribute("convertedAmount", convertedAmount);
        model.addAttribute("exchangeRate", exchangeRate);
        return "conversion-form";
    }
}
