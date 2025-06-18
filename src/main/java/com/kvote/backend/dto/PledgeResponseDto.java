package com.kvote.backend.dto;


import com.kvote.backend.domain.Pledge;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PledgeResponseDto {
    Long id;  // 공약 ID
    String description;  // 공약 내용
    Long candidateId;

    public static PledgeResponseDto from(Pledge pledge) {
        return PledgeResponseDto.builder()
                .id(pledge.getId())
                .description(pledge.getDescription())
                .candidateId(pledge.getCandidate().getId())
                .build();
    }
}
