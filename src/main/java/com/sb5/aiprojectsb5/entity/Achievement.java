package com.sb5.aiprojectsb5.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "required_count")
    private Integer requiredCount;

    @Column(name = "required_type", length = 50)
    private String requiredType; // какой тип мест нужно посещать

    @Column(name = "icon", length = 10)
    private String icon; // эмодзи или URL иконки
}
