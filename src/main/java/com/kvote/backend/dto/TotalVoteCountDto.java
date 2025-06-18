package com.kvote.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalVoteCountDto {
    @Schema(description = "선거 ID", example = "1001")
    Long electionId;
    @Schema(description = "총 투표 수", example = "5000")
    Long votedCount;
    @Schema(description = "총 유권자 수", example = "10000")
    Long voterCount;
    @Schema(description = "투표율", example = "50.0")
    Double turnoutRate; // 투표율
}
