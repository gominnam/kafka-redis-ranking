package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.dto.UserPlayCountDTO;
import com.example.kafkaredisranking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("select u.totalScore from User u where u.userId = ?1")
    Integer getScoreByUserId(String userId);

    @Modifying
    @Query("update User u set u.totalScore = u.totalScore + ?2 where u.userId = ?1")
    Integer addScoreByUserId(String userId, int score);

    @Query("select u.userId, u.userName, count(g) as playCount from User u join u.gamePlayStats g where u.userId = ?1 and CAST(g.playTime AS DATE) = CAST(?2 AS DATE) group by u.userId") // H2 Database Date casting issue
    Optional<UserPlayCountDTO> findByUserIdAndCountPlayTimeForToday(String userId, LocalDateTime playTime);

    Optional<User> findByUserId(String userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.gamePlayStats WHERE u.userId = :userId")
    Optional<User> findByUserIdWithStats(@Param("userId") String userId);
}
