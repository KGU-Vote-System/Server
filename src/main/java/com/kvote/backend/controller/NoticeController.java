package com.kvote.backend.controller;


import com.kvote.backend.dto.NoticeRequestDto;
import com.kvote.backend.dto.NoticeResponseDto;
import com.kvote.backend.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 생성
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@Valid @RequestBody NoticeRequestDto dto) {
        NoticeResponseDto response = noticeService.creatNotice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 단일 공지사항 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        NoticeResponseDto response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }

    // 모든 공지사항 조회 (최신순)
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        List<NoticeResponseDto> list = noticeService.getAllNotices();
        return ResponseEntity.ok(list);
    }

    // 공지사항 수정
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long id,
                                                          @Valid @RequestBody NoticeRequestDto dto) {
        NoticeResponseDto response = noticeService.updateNotice(id, dto);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}

