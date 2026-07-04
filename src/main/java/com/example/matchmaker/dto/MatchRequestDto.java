package com.example.matchmaker.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequestDto {
    private LocalDateTime matchDate;
    private String matchType; // '5vs5', '8vs8'
}