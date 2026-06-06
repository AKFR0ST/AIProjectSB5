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
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type; // 'exhibition', 'lecture_hall', 'cafe', 'shop', 'coworking'

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // Храните как "tag1,tag2,tag3"

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "is_free")
    @Builder.Default
    private Boolean isFree = true;
}