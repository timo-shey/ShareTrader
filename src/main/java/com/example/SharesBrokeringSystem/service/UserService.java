package com.example.SharesBrokeringSystem.service;

import com.example.SharesBrokeringSystem.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    void logout(HttpSession session);
    User findByEmailAndPassword(String email, String password);
    User findById(Long id);
    void save(User user);
}
