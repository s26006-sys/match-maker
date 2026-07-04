package com.example.matchmaker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "preferred_position", nullable = false, length = 10)
    private String preferredPosition;

    @Column(length = 10)
    private String tier;

    @Column(name = "internal_rating")
    private int internalRating;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public int getTierScore() {
        if ("상".equals(this.tier)) return 30;
        if ("하".equals(this.tier)) return 10;
        return 20;
    }
}