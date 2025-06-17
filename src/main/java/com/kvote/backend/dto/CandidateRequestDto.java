package com.kvote.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter
public class CandidateRequestDto {
    @Schema(description = "후보자 이름", example = "홍길동")
    private String name;  // 후보자 이름
    @Schema(description = "후보자 설명", example = "이 후보자는 훌륭한 리더입니다.")
    private String description;  // 후보자 설명")
    @Schema(description = "소속 선거 ID", example = "1")
    private Long electionId;  // 소속 선거 ID
}
