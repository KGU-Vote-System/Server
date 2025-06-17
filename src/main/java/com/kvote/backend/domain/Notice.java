package com.kvote.backend.domain;


import com.kvote.backend.dto.NoticeRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;


    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeStatus;

    @Enumerated(EnumType.STRING)
    private Campus campus;

    @Column(nullable = false)
    private LocalDate startAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate endAt;

    public NoticeStatus calculateStatus() {
        LocalDate now = LocalDate.now();

        if (now.isBefore(startAt)) return NoticeStatus.UPCOMING;
        if (now.isAfter(endAt)) return NoticeStatus.COMPLETED;
        return NoticeStatus.ONGOING;
    }

    public static Notice from(NoticeRequestDto dto) {
        return Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .noticeType(dto.getNoticeType())
                .campus(dto.getCampus())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .build();
    }

    public void updateFromDto(NoticeRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.noticeType = dto.getNoticeType();
        this.campus = dto.getCampus();
        this.startAt = dto.getStartAt();
        this.endAt = dto.getEndAt();
        // noticeStatus 필드는 업데이트 시 변경하지 않음 (동적 계산용)
    }


}



