package com.sb5.aiprojectsb5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_transactions")
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount; // положительные = начисление, отрицательные = списание

    @Column(name = "reason", length = 255)
    private String reason; // 'visit', 'feedback', 'purchase', 'achievement'

    @Column(name = "reference_id")
    private Integer referenceId; // ID связанной сущности (visit_track.id и т.д.)

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
