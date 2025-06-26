package com.kvote.backend.domain;

import com.kvote.backend.dto.ElectionRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "elections")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class Election {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date startAt;

    @Column(nullable = false)
    private Date endAt;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Campus campus;

    @Enumerated(EnumType.STRING)
    @Column(length = 66, nullable = false)
    private CollegeMajor collageMajorName;

    @Column(nullable = false)
    private String txHash;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public void update(ElectionRequestDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.startAt = dto.getStartAt();
        this.endAt = dto.getEndAt();
        this.isActive = true; // 선거가 생성될 때는 항상 활성화 상태로 설정
        this.campus = dto.getCampus();
    }

    public void updateActiveStatus(boolean b) {
        this.isActive = b;
    }

    public void startElection() {
        this.isActive = true;
        this.startAt = new Date();
    }
}
