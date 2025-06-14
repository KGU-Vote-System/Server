package com.kvote.backend.dto;

import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Nominee;
import com.kvote.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NomineeRequestDto {
    @Schema(description = "후보자 이름", example = "홍길동")
    private String name; // 후보자 이름
    @Schema(description = "후보자 학번", example = "20230001")
    private String studentId;
    @Schema(description = "후보자 소속 단과대학", example = "인문대학")
    private String college;
    @Schema(description = "후보자 소속 학과", example = "철학과")
    private String department;
    @Schema(description = "후보자 소개", example = "안녕하세요, 저는 홍길동입니다. ...")
    private String description;
    @Schema(description = "후보자 유형 (정후보: true, 부후보: false)", example = "true")
    private boolean isMain; // true = 정후보, false = 부후보

    public Nominee toEntity(Candidate candidate, User user) {
        return Nominee.builder()
                .name(name)
                .studentId(studentId)
                .college(college)
                .department(department)
                .description(description)
                .candidate(candidate) // Candidate 정보는 NomineeService에서 설정
                .isMain(isMain)
                .build();
    }
}
