package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Election;
import com.kvote.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectionRequestDto {
    @Schema(description = "선거 제목", example = "2023년도 총학생회 선거")
    private String title;

    @Schema(description = "선거 설명", example = "2023년도 총학생회 선거에 참여해주세요!")
    private String description;     // 선거 설명

    @Schema(description = "투표 시작 시각", example = "2023-10-01T00:00:00Z")
    private Date startAt;  // 투표 시작 시각

    @Schema(description = "투표 마감 시각", example = "2023-10-01T23:59:59Z")
    private Date endAt;    // 투표 마감 시각

    @Schema(description = "캠퍼스", example = "SEOUL or SUWON or ALL")
    private Campus campus;

    @Schema(description = "선거 활성 여부", example = "true")
    private boolean isActive; // 선거 활성화 상태, 기본값은 true

    @Schema(description = "선거 대상 학과 이름", example = "소프트웨어경영대학")
    private String collageMajorName; // 선거 대상 학과 이름, 선택적 필드

    public Election toEntity(BigInteger electionId, String txHash, User owner) {
        return Election.builder()
                .id(electionId.longValue())  // 계약에서 생성된 선거 ID, BigInteger를 Long으로 변환
                .title(this.title)
                .description(this.description)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .isActive(true)  // 기본값으로 활성화 상태
                .campus(this.campus)
                .owner(owner)
                .collageMajorName(this.collageMajorName)
                .txHash(txHash)
                .build();
    }
}
