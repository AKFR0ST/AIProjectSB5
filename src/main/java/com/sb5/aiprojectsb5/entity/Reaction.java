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
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false, unique = true)
    private VisitTrack track;

    @Column(name = "reaction_type", nullable = false, length = 20)
    private String reactionType; // 'positive', 'neutral', 'negative'

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
