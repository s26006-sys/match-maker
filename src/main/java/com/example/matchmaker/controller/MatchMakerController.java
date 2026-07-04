package com.example.matchmaker.controller;

import com.example.matchmaker.dto.MatchRequestDto;
import com.example.matchmaker.dto.PlayerRequestDto;
import com.example.matchmaker.dto.TeamMatchResultDto;
import com.example.matchmaker.service.MatchMakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchMakerController {

    private final MatchMakerService matchMakerService;

    /**
     * 1. 신규 선수 등록 API
     * POST http://localhost:8080/api/players
     */
    @PostMapping("/players")
    public ResponseEntity<String> registerPlayer(@RequestBody PlayerRequestDto requestDto) {
        Long playerId = matchMakerService.registerPlayer(requestDto);
        return ResponseEntity.ok("선수 등록 완료! ID: " + playerId);
    }

    /**
     * 2. 신규 경기 생성 API
     * POST http://localhost:8080/api/matches
     */
    @PostMapping("/matches")
    public ResponseEntity<String> createMatch(@RequestBody MatchRequestDto requestDto) {
        Long matchId = matchMakerService.createMatch(requestDto);
        return ResponseEntity.ok("경기 생성 완료! ID: " + matchId);
    }

    /**
     * 3. 경기 참가 신청 API
     * POST http://localhost:8080/api/matches/{matchId}/attend?playerId={playerId}
     */
    @PostMapping("/matches/{matchId}/attend")
    public ResponseEntity<String> attendMatch(
            @PathVariable Long matchId,
            @RequestParam Long playerId) {
        matchMakerService.attendMatch(matchId, playerId);
        return ResponseEntity.ok(playerId + "번 선수가 " + matchId + "번 경기에 참가 신청했습니다.");
    }

    /**
     * 4. 🔥 핵심: 팀 밸런스 매칭 실행 API
     * POST http://localhost:8080/api/matches/{matchId}/balance
     */
    @PostMapping("/matches/{matchId}/balance")
    public ResponseEntity<TeamMatchResultDto> balanceTeams(@PathVariable Long matchId) {
        TeamMatchResultDto result = matchMakerService.balanceTeams(matchId);
        return ResponseEntity.ok(result);
    }
}