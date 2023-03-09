package com.example.SharesBrokeringSystem.controller;

import com.example.SharesBrokeringSystem.model.Portfolio;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.service.ShareService;
import com.example.SharesBrokeringSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Objects;

@Controller
public class UserController {
    private final UserService userService;
    private final ShareService shareService;

    public UserController(UserService userService, ShareService shareService) {
        this.userService = userService;
        this.shareService = shareService;
    }

    @GetMapping("/")
    public String homePage(){
        return "homePage";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        User activeUser = userService.createUser(user);
        model.addAttribute("success", activeUser);
        return "registration_success";
    }

    @GetMapping("/login")
    public String UsersLoginForm(Model model, HttpServletRequest request){
        if(Objects.equals(request.getParameter("action"), "success")){
            model.addAttribute("success", true);
        }
        User users = new User();
        model.addAttribute("users", users);
        return "sign-in";
    }

    @PostMapping("/login")
    public String UserLogin(@ModelAttribute("users")User user, Model model, HttpServletRequest request, HttpServletResponse response){
        User activeUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if(activeUser != null){
            HttpSession httpSession = request.getSession(true);
            httpSession.setMaxInactiveInterval(360);
            httpSession.setAttribute("id", activeUser.getUserId());
            httpSession.setAttribute("name", activeUser.getName());
            User currentUser = userService.findById(activeUser.getUserId());
            model.addAttribute("user", currentUser);
            Long userId = activeUser.getUserId();
            return "redirect:/home/users/" + userId;
        }
        return "redirect:/login";
    }

    @GetMapping("/home/users/{id}")
    public String home(HttpSession session, Model model, HttpServletRequest request, @PathVariable Long id) {
        Object userDetails = session.getAttribute("name");
        if (userDetails != null) {
            User user = userService.findById(id);
            double walletBalance = user.getWallet();
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            String formattedBalance = formatter.format(walletBalance);
            model.addAttribute("walletBalance", formattedBalance);
            User activeUser = userService.findById(id);
            model.addAttribute("user", activeUser);
            Portfolio portfolio = activeUser.getPortfolio();
            model.addAttribute("portfolio", portfolio);
            model.addAttribute("shareService", shareService);
            return "home";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }

}
