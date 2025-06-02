package com.kvote.backend.domain;

import com.kvote.backend.domain.Election;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidates")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long voteCount; // 투표 수

    @Column(nullable = false)
    private Long electionId;  // 소속 선거 ID (Election 엔티티의 id)

    public void updateVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
