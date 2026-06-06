package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByBadgeId(String badgeId);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByTelegramId(String telegramId);
    
    @Query("SELECT u FROM User u WHERE u.badgeId = :badgeId OR u.phone = :phone")
    Optional<User> findByBadgeIdOrPhone(@Param("badgeId") String badgeId, @Param("phone") String phone);
    
    @Query("SELECT COUNT(v) FROM VisitSession v WHERE v.user.id = :userId")
    long countVisitsByUserId(@Param("userId") UUID userId);
}