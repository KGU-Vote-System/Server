package com.kvote.backend.repository;

import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Pledge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PledgeRepository extends JpaRepository<Pledge, Long> {

    // 특정 후보자의 공약을 조회하는 메소드
    List<Pledge> findByCandidateId(Long candidateId);

    // 특정 후보자의 공약을 삭제하는 메소드
    void deleteByCandidateId(Long candidateId);

    List<Pledge> findAllByCandidate(Candidate candidate);
}
