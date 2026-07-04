package com.example.matchmaker.entity; // 패키지 경로 일치 확인!

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_attendance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MatchAttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match; // MatchEntity 타입으로 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player; // PlayerEntity 타입으로 연결

    @Setter
    @Column(name = "assigned_team", length = 10)
    private String assignedTeam;
}