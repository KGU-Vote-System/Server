package com.kvote.backend.domain;

import com.kvote.backend.dto.PledgeRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pledges")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    public void update(PledgeRequestDto pledgeRequestDto) {
        this.description = pledgeRequestDto.getDescription();
    }
}

