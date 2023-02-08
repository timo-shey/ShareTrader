package com.example.SharesBrokeringSystem.repository;

import com.example.SharesBrokeringSystem.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
}
