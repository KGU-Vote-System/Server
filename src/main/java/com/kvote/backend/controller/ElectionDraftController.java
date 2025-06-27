package com.kvote.backend.controller;

import com.kvote.backend.dto.ElectionAllRequestDto;
import com.kvote.backend.service.ElectionDraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/election-drafts")
@PreAuthorize("hasRole('ADMIN')")
public class ElectionDraftController {

    private final ElectionDraftService eds;

    // CREATE
    @PostMapping
    public ResponseEntity<ElectionAllRequestDto> create(@RequestBody ElectionAllRequestDto dto) {
        return ResponseEntity.ok(eds.saveAndReturnAsRequestDto(dto));
    }

    // READ + DELETE (단건)
    @GetMapping("/{id}")
    public ResponseEntity<ElectionAllRequestDto> getAndDelete(@PathVariable Long id) {
        return ResponseEntity.ok(eds.findAndDeleteByIdAsRequestDto(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ElectionAllRequestDto> update(
            @PathVariable Long id,
            @RequestBody ElectionAllRequestDto dto) {
        return ResponseEntity.ok(eds.updateAndReturnAsRequestDto(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eds.delete(id);
        return ResponseEntity.noContent().build();
    }
}
