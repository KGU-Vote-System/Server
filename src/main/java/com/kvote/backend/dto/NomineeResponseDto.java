package com.kvote.backend.dto;

import com.kvote.backend.domain.Nominee;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NomineeResponseDto {
    private Long id;
    private String name; // 후보자 이름
    private String studentId;
    private String college;
    private String department;
    private String description;
    private Long candidateId;
    private boolean isMain; // true = 정후보, false = 부후보

    public static NomineeResponseDto from(Nominee nominee) {
        return NomineeResponseDto.builder()
                .id(nominee.getId())
                .name(nominee.getName())
                .studentId(nominee.getStudentId())
                .college(nominee.getCollege())
                .department(nominee.getDepartment())
                .description(nominee.getDescription())
                .candidateId(nominee.getCandidate().getId())
                .isMain(nominee.isMain())
                .build();
    }
}
