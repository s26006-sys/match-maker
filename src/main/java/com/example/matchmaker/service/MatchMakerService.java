package com.example.matchmaker.service;

import com.example.matchmaker.dto.MatchRequestDto;
import com.example.matchmaker.dto.PlayerRequestDto;
import com.example.matchmaker.dto.TeamMatchResultDto;
import com.example.matchmaker.entity.MatchAttendanceEntity;
import com.example.matchmaker.entity.MatchEntity;
import com.example.matchmaker.entity.PlayerEntity;
import com.example.matchmaker.exception.ResourceNotFoundException;
import com.example.matchmaker.repository.MatchAttendanceRepository;
import com.example.matchmaker.repository.MatchRepository;
import com.example.matchmaker.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchMakerService {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchAttendanceRepository matchAttendanceRepository;

    /**
     * 1. 신규 선수 등록
     */
    @Transactional
    public Long registerPlayer(PlayerRequestDto requestDto) {
        PlayerEntity player = PlayerEntity.builder()
                .name(requestDto.getName())
                .preferredPosition(requestDto.getPreferredPosition())
                .tier(requestDto.getTier())
                .internalRating(requestDto.getInternalRating())
                .build();
        return playerRepository.save(player).getId();
    }

    /**
     * 2. 신규 경기(매치) 생성
     */
    @Transactional
    public Long createMatch(MatchRequestDto requestDto) {
        MatchEntity match = MatchEntity.builder()
                .matchDate(requestDto.getMatchDate())
                .matchType(requestDto.getMatchType())
                .status("SCHEDULED")
                .build();
        return matchRepository.save(match).getId();
    }

    /**
     * 3. 특정 매치에 선수 참가 신청 처리
     */
    @Transactional
    public void attendMatch(Long matchId, Long playerId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 경기를 찾을 수 없습니다. ID: " + matchId));
        PlayerEntity player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 선수를 찾을 수 없습니다. ID: " + playerId));

        MatchAttendanceEntity attendance = MatchAttendanceEntity.builder()
                .match(match)
                .player(player)
                .build();
        matchAttendanceRepository.save(attendance);
    }

    /**
     * 4. 🔥 핵심: 지그재그 밸런스 매칭 알고리즘 실행
     */
    @Transactional
    public TeamMatchResultDto balanceTeams(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 경기를 찾을 수 없습니다. ID: " + matchId));

        // 해당 매치에 참여 신청한 명단 전부 긁어오기
        List<MatchAttendanceEntity> attendances = matchAttendanceRepository.findByMatchId(matchId);

        if (attendances.isEmpty()) {
            throw new IllegalArgumentException("참가한 선수가 없습니다.");
        }

        // 실력 총합 점수(Rating + 티어 점수)가 높은 순서대로 내림차순 정렬
        attendances.sort((a, b) -> {
            int scoreA = a.getPlayer().getInternalRating() + a.getPlayer().getTierScore();
            int scoreB = b.getPlayer().getInternalRating() + b.getPlayer().getTierScore();
            return Integer.compare(scoreB, scoreA); // 내림차순 정렬
        });

        List<MatchAttendanceEntity> teamAAttendances = new ArrayList<>();
        List<MatchAttendanceEntity> teamBAttendances = new ArrayList<>();
        int totalScoreA = 0;
        int totalScoreB = 0;

        // 지그재그 분배 알고리즘 (실력 총합이 더 낮은 팀에 다음 순위 선수를 쏙 배치)
        for (MatchAttendanceEntity attendance : attendances) {
            int currentScore = attendance.getPlayer().getInternalRating() + attendance.getPlayer().getTierScore();

            if (totalScoreA <= totalScoreB) {
                attendance.setAssignedTeam("A");
                teamAAttendances.add(attendance);
                totalScoreA += currentScore;
            } else {
                attendance.setAssignedTeam("B");
                teamBAttendances.add(attendance);
                totalScoreB += currentScore;
            }
        }

        // 결과 DTO 변환 및 반환
        return TeamMatchResultDto.builder()
                .matchId(match.getId())
                .totalScoreA(totalScoreA)
                .totalScoreB(totalScoreB)
                .teamA(teamAAttendances.stream().map(this::convertToResponseDto).collect(Collectors.toList()))
                .teamB(teamBAttendances.stream().map(this::convertToResponseDto).collect(Collectors.toList()))
                .build();
    }

    private TeamMatchResultDto.PlayerResponseDto convertToResponseDto(MatchAttendanceEntity attendance) {
        PlayerEntity player = attendance.getPlayer();
        return TeamMatchResultDto.PlayerResponseDto.builder()
                .id(player.getId())
                .name(player.getName())
                .preferredPosition(player.getPreferredPosition())
                .tier(player.getTier())
                .build();
    }
}