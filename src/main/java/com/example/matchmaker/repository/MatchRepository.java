package com.example.matchmaker.repository;

import com.example.matchmaker.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    // 이제 스프링이 이 안을 데이터 저장/조회 기능으로 알아서 채워줍니다!
}