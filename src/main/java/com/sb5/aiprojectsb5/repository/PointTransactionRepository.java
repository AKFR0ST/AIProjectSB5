// PointTransactionRepository
package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Integer> {
    
    List<PointTransaction> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    @Query("SELECT SUM(pt.amount) FROM PointTransaction pt WHERE pt.user.id = :userId")
    Integer getTotalPointsEarned(@Param("userId") UUID userId);
    
    List<PointTransaction> findByUserIdAndReason(UUID userId, String reason);
}