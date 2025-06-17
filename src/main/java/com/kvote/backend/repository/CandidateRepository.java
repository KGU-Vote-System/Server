package com.kvote.backend.repository;

import com.kvote.backend.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // Define custom query methods if needed
    // For example, find candidates by election ID
    List<Candidate> findByElectionId(Long electionId);
}
