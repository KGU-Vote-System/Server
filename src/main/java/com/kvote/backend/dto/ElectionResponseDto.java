package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import lombok.*;

import java.util.Date;

@Data
@Builder
public class ElectionResponseDto {

    private Long id;
    private String contractElectionId;
    private String title;
    private String description;
    private Date startAt;
    private Date endAt;
    private Boolean isActive;
    private Campus campus;
    private Long ownerId;
}
