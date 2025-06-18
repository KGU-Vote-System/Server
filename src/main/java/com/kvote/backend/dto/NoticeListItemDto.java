package com.kvote.backend.dto;

import com.kvote.backend.domain.Notice;
import com.kvote.backend.domain.NoticeStatus;
import com.kvote.backend.domain.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
public class NoticeListItemDto {
    private Long id;
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private NoticeStatus noticeStatus;

    public static NoticeListItemDto from(Notice notice) {
        NoticeStatus status;
        if (notice.getNoticeType() == NoticeType.NOTIFY) {
            status = NoticeStatus.NOTIFY;  // 알림은 고정 상태
        } else {
            status = notice.calculateStatus();  // 선거일시 동적 계산
        }

        return NoticeListItemDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .noticeStatus(status)
                .build();


    }
}