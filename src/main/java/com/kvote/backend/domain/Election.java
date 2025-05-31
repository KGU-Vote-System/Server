package com.kvote.backend.domain;

import com.kvote.backend.dto.ElectionResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "elections")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class Election {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date startAt;

    @Column(nullable = false)
    private Date endAt;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Campus campus;

    @Column(nullable = false)
    private String txHash;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
