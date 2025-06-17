package com.kvote.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalVoteCountDto {
    Long electionId;
    Long voteCount;
}
