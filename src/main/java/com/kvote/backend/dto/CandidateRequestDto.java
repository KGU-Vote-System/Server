package com.kvote.backend.dto;

import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter
public class CandidateRequestDto {
    private String name;  // 후보자 이름
    private String description;  // 후보자 설명
    private Long electionId;  // 소속 선거 ID
}
