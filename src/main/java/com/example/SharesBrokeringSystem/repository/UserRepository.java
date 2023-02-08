package com.example.SharesBrokeringSystem.repository;

import com.example.SharesBrokeringSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
