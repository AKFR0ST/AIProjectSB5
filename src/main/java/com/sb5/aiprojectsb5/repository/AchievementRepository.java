package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    
    Optional<Achievement> findByName(String name);
    
    List<Achievement> findByRequiredType(String requiredType);
}

