package com.kvote.backend.service;

import com.kvote.backend.domain.ElectionDraft;
import com.kvote.backend.repository.ElectionDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionDraftService {
    private final ElectionDraftRepository electionDraftRepository;


    public ElectionDraft save(ElectionDraft draft) {
        return electionDraftRepository.save(draft);
    }

    public ElectionDraft findById(Long id) {
        return electionDraftRepository.findById(id).orElseThrow(() -> new RuntimeException("Draft not found"));
    }

    public List<ElectionDraft> findAll() {
        return electionDraftRepository.findAll();
    }

    public ElectionDraft update(Long id, ElectionDraft updatedDraft) {
        ElectionDraft draft = findById(id);
        updatedDraft.setId(draft.getId());
        return electionDraftRepository.save(updatedDraft);
    }

    public void delete(Long id) {
        electionDraftRepository.deleteById(id);
    }
}

