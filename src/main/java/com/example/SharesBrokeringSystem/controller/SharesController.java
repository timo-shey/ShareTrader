package com.example.SharesBrokeringSystem.controller;

import com.example.SharesBrokeringSystem.model.Portfolio;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.repository.PortfolioRepository;
import com.example.SharesBrokeringSystem.service.ShareService;
import com.example.SharesBrokeringSystem.service.UserService;
import io.finnhub.api.models.CompanyNews;
import io.finnhub.api.models.CompanyProfile;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/shares")
public class SharesController {

    private final ShareService shareService;
    private final UserService userService;
    private final PortfolioRepository portfolioRepository;

    public SharesController(ShareService shareService, UserService userService,
                            PortfolioRepository portfolioRepository) {
        this.shareService = shareService;
        this.userService = userService;
        this.portfolioRepository = portfolioRepository;
    }

    @GetMapping("/buy")
    public String buyShares( String symbol, Integer quantity, Model model, HttpSession session) {
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
        return "buy-shares";
    }

    @PostMapping("/buy")
    public String postBuyShare(@RequestParam(name = "symbol") String symbol,
                               @RequestParam(name = "quantity") Integer quantity,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);

        double currentPrice = shareService.getStockPrice(symbol);
        double totalCost = currentPrice * quantity;

        boolean success = shareService.purchaseShare(symbol, quantity, user);

        if (success) {
            model.addAttribute("message", "Successfully bought " + quantity + " shares of " + symbol + " for $" + totalCost);
        } else {
            model.addAttribute("message", "Failed to buy shares");
        }
        redirectAttributes.addFlashAttribute("successMessage", "Successfully bought shares of " + symbol);
        return "redirect:/home/users/" + userId;
    }
    @GetMapping("/sell")
    public String sellShare(String symbol, Integer quantity, Model model, HttpSession session) {
        Object userDetails = session.getAttribute("name");
        if (userDetails == null) {
            return "redirect:/login";
        }

        Long id = (Long) session.getAttribute("id");
        User user = userService.findById(id);
        double walletBalance = user.getWallet();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedBalance = formatter.format(walletBalance);
        model.addAttribute("walletBalance", formattedBalance);
        User activeUser = userService.findById(id);
        model.addAttribute("user", activeUser);
        Portfolio portfolio = activeUser.getPortfolio();
        if (portfolio == null) {
            portfolio = new Portfolio();
        }
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("shareService", shareService);
        List<String> ownedSymbols = new ArrayList<>(portfolio.getHoldings().keySet());
        model.addAttribute("ownedSymbols", ownedSymbols);
        return "sell-shares";
    }

    @PostMapping("/sell")
    public String postSellShare(@RequestParam("symbol") String symbol,
                                @RequestParam("quantity") Integer quantity,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        User user = userService.findById(userId);

        double currentPrice = shareService.getStockPrice(symbol);
        double totalCost = currentPrice * quantity;

        boolean success = shareService.sellShare(symbol, quantity, user);

        if (success) {
            model.addAttribute("message", "Successfully sold " + quantity + " shares of " + symbol + " for $" + totalCost);

        } else {
            model.addAttribute("message", "Failed to sell shares");
        }

        redirectAttributes.addFlashAttribute("successMessage", "Successfully sold shares of " + symbol);

        return "redirect:/home/users/" + userId;
    }

    @GetMapping("/company-profile")
    public String getCompanyProfile(@RequestParam(name = "symbol", required = false) String symbol, Model model, HttpSession session) {
        if (symbol != null) {
            try {
                CompanyProfile companyProfile = shareService.getCompanyProfile(symbol);
                model.addAttribute("companyProfile", companyProfile);
                List<CompanyNews> companyNews = shareService.getCompanyNews(symbol, LocalDate.now().minusDays(1), LocalDate.now());
                model.addAttribute("companyNews", companyNews);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "redirect:/shares/company-profile"; // redirect to same page if symbol is not valid
            }
        }
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
        return "company-profile";
    }

    @PostMapping("/company-profile")
    public String postCompanyProfile(@RequestParam(name = "symbol") String symbol) {
        return "redirect:/shares/company-profile?symbol=" + symbol;
    }
}
