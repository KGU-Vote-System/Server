package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.CollegeMajor;
import com.kvote.backend.domain.Election;
import lombok.*;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectionResponseDto {

    private Long id;
//    private String contractElectionId;
    private String title;
    private String description;
    private Date startAt;
    private Date endAt;
    private Boolean isActive;
    private Campus campus;
    private Long ownerId;
    private CollegeMajor collageMajorName;

    public static ElectionResponseDto from(Election election) {
        return ElectionResponseDto.builder()
                .id(election.getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .isActive(election.getIsActive())
                .campus(election.getCampus())
                .ownerId(election.getOwner().getId())
                .collageMajorName(election.getCollageMajorName())
                .build();
    }
}
