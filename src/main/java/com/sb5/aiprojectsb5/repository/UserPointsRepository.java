package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.UserPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface UserPointsRepository extends JpaRepository<UserPoints, UUID> {

    Optional<UserPoints> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserPoints up SET up.balance = up.balance + :amount, up.updatedAt = CURRENT_TIMESTAMP WHERE up.userId = :userId")
    int addPoints(@Param("userId") UUID userId, @Param("amount") Integer amount);
}


