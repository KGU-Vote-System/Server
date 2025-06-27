package com.kvote.backend.repository;

import com.kvote.backend.domain.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    boolean existsById(Long id);
    Optional<Election> findById(Long id);

    // 활성화된 선거 목록을 조회하는 메소드
    List<Election> findByIsActiveTrue();

    List<Election> findByStartAtBetween(Date from, Date to);
  
    //종료된 선거 목록을 조회하는 메서드
    List<Election> findByIsActiveFalse();

    List<Election> findByIsActiveTrueAndStartAtAfter(Date startAt);

    List<Election> findByIsActiveTrueAndStartAtBefore(Date startAt);
}