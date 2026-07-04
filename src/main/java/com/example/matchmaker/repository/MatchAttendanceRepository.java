package com.example.matchmaker.repository;

import com.example.matchmaker.entity.MatchAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchAttendanceRepository extends JpaRepository<MatchAttendanceEntity, Long> {

    // 특정 경기에 참가하는 명단만 쏙 뽑아오기 위한 커스텀 메서드
    List<MatchAttendanceEntity> findByMatchId(Long matchId);
}