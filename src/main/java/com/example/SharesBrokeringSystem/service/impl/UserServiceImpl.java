package com.example.SharesBrokeringSystem.service.impl;

import com.example.SharesBrokeringSystem.exeption.UserNotFoundException;
import com.example.SharesBrokeringSystem.model.User;
import com.example.SharesBrokeringSystem.repository.UserRepository;
import com.example.SharesBrokeringSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        user.setWallet(0.00);
        return userRepository.save(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.removeAttribute("userDetails");
        session.invalidate();
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        return user.orElse(null);
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException("User Not Found");
        return user.get();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
