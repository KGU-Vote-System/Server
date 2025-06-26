package com.kvote.backend.controller;

import com.kvote.backend.domain.ElectionDraft;
import com.kvote.backend.service.ElectionDraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/election-drafts")
@PreAuthorize("hasRole('ADMIN')")
public class ElectionDraftController {
    private final ElectionDraftService eds;

    @PostMapping
    public ResponseEntity<ElectionDraft> create(@RequestBody ElectionDraft draft) {
        return ResponseEntity.ok(eds.save(draft));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectionDraft> getAndDelete(@PathVariable Long id) {
        ElectionDraft draft = eds.findById(id);
        eds.delete(id); // 조회 후 즉시 삭제
        return ResponseEntity.ok(draft);
    }


    @GetMapping
    public ResponseEntity<List<ElectionDraft>> getAll() {
        return ResponseEntity.ok(eds.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElectionDraft> update(@PathVariable Long id, @RequestBody ElectionDraft draft) {
        return ResponseEntity.ok(eds.update(id, draft));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eds.delete(id);
        return ResponseEntity.noContent().build();
    }

}

