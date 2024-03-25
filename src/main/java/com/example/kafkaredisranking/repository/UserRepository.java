package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("select u.totalScore from User u where u.userId = ?1")
    int getScoreByUserId(String userId);
    @Transactional
    @Modifying
    @Query("update User u set u.totalScore = u.totalScore + ?2 where u.userId = ?1")
    void addScoreByUserId(String userId, int score);
}