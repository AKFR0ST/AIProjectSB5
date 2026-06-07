package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.SavedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<SavedRoute, UUID> {

    List<SavedRoute> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<SavedRoute> findBySessionId(UUID sessionId);

    @Query("SELECT sr FROM SavedRoute sr WHERE sr.user.id = :userId AND sr.isCompleted = false ORDER BY sr.createdAt DESC")
    List<SavedRoute> findActiveRoutesByUser(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE SavedRoute sr SET sr.isCompleted = true, sr.completedAt = :completedAt WHERE sr.id = :routeId")
    void markAsCompleted(@Param("routeId") UUID routeId, @Param("completedAt") LocalDateTime completedAt);
}
