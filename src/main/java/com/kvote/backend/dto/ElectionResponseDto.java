package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import lombok.*;

import java.util.Date;

@Data
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

    public static ElectionResponseDto from(com.kvote.backend.domain.Election election) {
        return ElectionResponseDto.builder()
                .id(election.getId())
//                .contractElectionId(election.getContractElection().getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .isActive(election.getIsActive())
                .campus(election.getCampus())
                .ownerId(election.getOwner().getId())
                .build();
    }
}
