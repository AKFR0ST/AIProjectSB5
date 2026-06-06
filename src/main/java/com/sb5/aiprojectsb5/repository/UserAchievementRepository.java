// UserAchievementRepository
package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {

    List<UserAchievement> findByUserId(UUID userId);

    List<UserAchievement> findByUserIdAndCompletedTrue(UUID userId);

    Optional<UserAchievement> findByUserIdAndAchievementId(UUID userId, Integer achievementId);

    @Modifying
    @Transactional
    @Query("UPDATE UserAchievement ua SET ua.progress = :progress WHERE ua.user.id = :userId AND ua.achievement.id = :achievementId")
    void updateProgress(@Param("userId") UUID userId, @Param("achievementId") Integer achievementId, @Param("progress") Integer progress);
}