package com.sb5.aiprojectsb5.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recommended_places")
    private String recommendedPlaces; // JSON строка с массивом ID мест

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "route_data", columnDefinition = "TEXT")
    private String routeData; // полный JSON маршрута

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_viewed")
    @Builder.Default
    private Boolean isViewed = false;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // срок действия рекомендации
}
