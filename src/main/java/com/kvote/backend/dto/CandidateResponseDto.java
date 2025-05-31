package com.kvote.backend.dto;

import com.kvote.backend.domain.Candidate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateResponseDto {
    private Long id; // 후보 ID
    private String name; // 선거운동부 이름
    private Long electionId; // 소속 선거 ID
    private Long voteCount; // 투표 수
}
