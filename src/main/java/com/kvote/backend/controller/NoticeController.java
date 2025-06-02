package com.kvote.backend.controller;


import com.kvote.backend.domain.Campus;
import com.kvote.backend.dto.NoticeRequestDto;
import com.kvote.backend.dto.NoticeResponseDto;
import com.kvote.backend.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "create a new Notice")
    public ResponseEntity<NoticeResponseDto> createNotice(@Valid @RequestBody NoticeRequestDto dto) {
        NoticeResponseDto response = noticeService.creatNotice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 단일 공지사항 조회
    @GetMapping("/{id}")
    @Operation(summary = "read a single notice")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        NoticeResponseDto response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/notices/campus/{campus}")
    @Operation(summary = "read notices by campus ")
    public ResponseEntity<List<NoticeResponseDto>> getNoticesByCampus(@PathVariable Campus campus) {
        List<NoticeResponseDto> notices = noticeService.getNoticesByCampus(campus);
        return ResponseEntity.ok(notices);
    }


    // 공지사항 수정
    @PutMapping("/{id}")
    @Operation(summary = "update a notice")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long id,
                                                          @Valid @RequestBody NoticeRequestDto dto) {
        NoticeResponseDto response = noticeService.updateNotice(id, dto);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "delete a notice")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}

