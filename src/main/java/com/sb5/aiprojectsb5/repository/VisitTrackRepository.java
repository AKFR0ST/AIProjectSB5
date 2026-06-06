package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.VisitTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitTrackRepository extends JpaRepository<VisitTrack, Integer> {
    
    List<VisitTrack> findBySessionId(UUID sessionId);
    
    @Query("SELECT vt FROM VisitTrack vt WHERE vt.session.user.id = :userId")
    List<VisitTrack> findAllByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT vt FROM VisitTrack vt WHERE vt.session.user.id = :userId AND vt.scannedAt >= :since")
    List<VisitTrack> findUserTracksSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);
    
    @Query("SELECT vt.place.id, COUNT(vt) FROM VisitTrack vt WHERE vt.session.user.id = :userId GROUP BY vt.place.id ORDER BY COUNT(vt) DESC")
    List<Object[]> findMostVisitedPlacesByUser(@Param("userId") UUID userId);
    
    boolean existsBySessionIdAndPlaceId(UUID sessionId, Integer placeId);
}