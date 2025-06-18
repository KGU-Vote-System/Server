package com.kvote.backend.controller;


import com.kvote.backend.domain.Campus;
import com.kvote.backend.dto.NoticeRequestDto;
import com.kvote.backend.dto.NoticeDetailDto;
import com.kvote.backend.dto.NoticeListItemDto;
import com.kvote.backend.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "create a new Notice")
    public ResponseEntity<NoticeDetailDto> createNotice(@Valid @RequestBody NoticeRequestDto dto) {
        NoticeDetailDto response = noticeService.creatNotice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 단일 공지사항 조회
    @GetMapping("/{id}")
    @Operation(summary = "read a single notice")
    public ResponseEntity<NoticeDetailDto> getNotice(@PathVariable Long id) {
        NoticeDetailDto response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }

    //캠퍼스별 공지사항 리스트 조회 (알람 공지사항은 수원/서울 어디에서든지 항상 최상단, ALL인 경우 수원/서울 두군데에서 모두 조회가능해야함)
    @GetMapping("/notices/campus/{campus}")
    @Operation(summary = "read notices by campus ")
    public ResponseEntity<List<NoticeListItemDto>> getNoticesByCampus(@PathVariable Campus campus) {
        List<NoticeListItemDto> notices = noticeService.getNoticesByCampus(campus);
        return ResponseEntity.ok(notices);
    }


    // 공지사항 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "update a notice")
    public ResponseEntity<NoticeDetailDto> updateNotice(@PathVariable Long id,
                                                        @Valid @RequestBody NoticeRequestDto dto) {
        NoticeDetailDto response = noticeService.updateNotice(id, dto);
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
