package com.kvote.backend.dto;

import com.kvote.backend.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@Getter
public class NoticeDetailDto {
    private Long id;
    private String title;
    private String content;
    private LocalDate createdAt;

    public static NoticeDetailDto from(Notice notice){
        return NoticeDetailDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}