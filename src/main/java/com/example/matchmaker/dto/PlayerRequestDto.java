package com.example.matchmaker.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRequestDto {
    private String name;
    private String preferredPosition; // 'FW', 'MF', 'DF', 'GK'
    private String tier;              // '상', '중', '하'
    private int internalRating;        // 실력 점수
}