package com.kvote.backend.repository;

import com.kvote.backend.domain.ElectionDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionDraftRepository extends JpaRepository<ElectionDraft, Long> {
}
