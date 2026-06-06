package com.sb5.aiprojectsb5.dto.llm;

import com.sb5.aiprojectsb5.entity.Reaction;
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
@Table(name = "reaction_tags")
public class ReactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_id", nullable = false)
    private Reaction reaction;

    @Column(name = "tag", nullable = false, length = 100)
    private String tag;
}