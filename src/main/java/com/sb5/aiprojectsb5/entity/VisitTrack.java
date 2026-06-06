package com.sb5.aiprojectsb5.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visit_tracks")
public class VisitTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private VisitSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "scanned_at", nullable = false)
    @Builder.Default
    private LocalDateTime scannedAt = LocalDateTime.now();

    @Column(name = "qr_code", length = 100)
    private String qrCode;

    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Reaction reaction;
}
