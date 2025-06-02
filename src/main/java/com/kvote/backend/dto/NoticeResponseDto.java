package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Notice;
import com.kvote.backend.domain.NoticeStatus;
import com.kvote.backend.domain.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private NoticeType noticeType;
    private Campus campus;
    private Date createdAt;
    private NoticeStatus noticeStatus;
    private Date startAt;
    private Date endAt;

    public static NoticeResponseDto from(Notice notice) {
        NoticeStatus status;
        if (notice.getNoticeType() == NoticeType.NOTIFY) {
            status = NoticeStatus.NOTIFY;  // 알림은 고정 상태
        } else {
            status = notice.calculateStatus();  // 선거 등은 동적 계산
        }

        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .noticeType(notice.getNoticeType())
                .campus(notice.getCampus())
                .createdAt(notice.getCreatedAt())
                .noticeStatus(status)
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .build();
    }
}
