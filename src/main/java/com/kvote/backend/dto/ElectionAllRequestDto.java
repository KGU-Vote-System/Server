package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectionAllRequestDto {

    private ElectionDto election;
    private CandidateDto candidate1;
    private CandidateDto candidate2;

    @Getter @Setter @Builder
    public static class ElectionDto {
        private String title;
        private String description;
        private Date startAt;
        private Date endAt;
        private Campus campus;
        private String collageMajorName;
    }

    @Getter
    @Setter
    @Builder
    public static class CandidateDto {
        private Info info;
        private NomineeDto nominee1;
        private NomineeDto nominee2;

        @Getter @Setter
        @Builder
        public static class Info {
            private String name;
            private Long electionId; // 저장 시는 null로 둘 수도 있음
        }

        @Getter @Setter
        @Builder
        public static class NomineeDto {
            private String name;
            private String studentId;
            private String college;
            private String department;
            private boolean main;
            private String description1;
            private String description2;
            private String description3;
        }
    }
}
