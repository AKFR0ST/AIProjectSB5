package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.VisitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitSessionRepository extends JpaRepository<VisitSession, UUID> {
    
    List<VisitSession> findByUserIdOrderByStartedAtDesc(UUID userId);
    
    Optional<VisitSession> findFirstByUserIdAndEndedAtIsNullOrderByStartedAtDesc(UUID userId);
    
    @Query("SELECT vs FROM VisitSession vs WHERE vs.user.id = :userId AND vs.startedAt >= :startDate")
    List<VisitSession> findUserSessionsFromDate(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(DISTINCT vt.place.id) FROM VisitSession vs JOIN vs.tracks vt WHERE vs.user.id = :userId")
    long countDistinctVisitedPlacesByUser(@Param("userId") UUID userId);
}