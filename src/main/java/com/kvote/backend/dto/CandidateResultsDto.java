package com.kvote.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateResultsDto {
    @Schema(description = "후보자 ID", example = "1")
    private Long id; // 후보자 ID
    @Schema(description = "후보자 이름", example = "홍길동")
    private String name; // 후보자 이름
    @Schema(description = "선거 ID", example = "1001")
    private Long electionId; // 선거 ID
    @Schema(description = "득표 수", example = "1500")
    private Long voteCount; // 득표 수
    @Schema(description = "득표율", example = "75.0")
    private Double votePercentage; // 득표율
}
