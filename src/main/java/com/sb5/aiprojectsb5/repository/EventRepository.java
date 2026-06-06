package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    
    List<Event> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime after);
    
    List<Event> findByType(String type);
    
    @Query("SELECT e FROM Event e WHERE e.startTime BETWEEN :start AND :end ORDER BY e.startTime")
    List<Event> findEventsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE e.place.id = :placeId AND e.startTime > :now ORDER BY e.startTime")
    List<Event> findUpcomingEventsByPlace(@Param("placeId") Integer placeId, @Param("now") LocalDateTime now);
}