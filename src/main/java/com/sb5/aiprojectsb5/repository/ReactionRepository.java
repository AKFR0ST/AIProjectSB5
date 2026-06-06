package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    
    List<Reaction> findByTrackSessionUserId(UUID userId);
    
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.track.session.user.id = :userId GROUP BY r.reactionType")
    List<Object[]> getReactionStatsByUser(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM Reaction r WHERE r.track.place.id = :placeId AND r.reactionType = :type")
    List<Reaction> findByPlaceAndType(@Param("placeId") Integer placeId, @Param("type") String type);
}