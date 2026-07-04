package com.example.matchmaker.dto;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMatchResultDto {
    private Long matchId;
    private List<PlayerResponseDto> teamA;
    private List<PlayerResponseDto> teamB;
    private int totalScoreA; // A팀 실력 총합
    private int totalScoreB; // B팀 실력 총합

    // 내부에 들어가는 선수 응답 정보도 PlayerResponseDto로 이름 변경
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlayerResponseDto {
        private Long id;
        private String name;
        private String preferredPosition;
        private String tier;
    }
}