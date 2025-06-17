package com.kvote.backend.dto;

import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Pledge;
import com.kvote.backend.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PledgeRequestDto {
    String description;  // 공약 내용

    public Pledge toEntity(Candidate candidate) {
        return Pledge.builder()
                .description(description)
                .candidate(candidate)
                .build();
    }
}
