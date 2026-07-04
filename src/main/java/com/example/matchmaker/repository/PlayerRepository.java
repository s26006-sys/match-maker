package com.example.matchmaker.repository;

import com.example.matchmaker.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    // 기본 크루드(등록, 수정, 삭제, ID조회) 기능이 자동으로 탑재됩니다.
}