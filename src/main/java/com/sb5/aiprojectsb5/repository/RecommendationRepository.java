package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    
    List<Recommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    Optional<Recommendation> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Recommendation r SET r.isViewed = true WHERE r.id = :id")
    void markAsViewed(@Param("id") Integer id);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Recommendation r WHERE r.expiresAt < :now")
    void deleteExpired(@Param("now") LocalDateTime now);
}