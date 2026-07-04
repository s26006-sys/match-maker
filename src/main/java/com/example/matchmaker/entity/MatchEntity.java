package com.example.matchmaker.entity; // 패키지 경로 일치 확인!

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Column(name = "match_type", length = 20)
    private String matchType;

    @Column(length = 20)
    private String status;

    @Column(name = "score_a")
    private Integer scoreA;

    @Column(name = "score_b")
    private Integer scoreB;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}