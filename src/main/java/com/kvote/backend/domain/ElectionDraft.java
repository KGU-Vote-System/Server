package com.kvote.backend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 선거 정보
    private String title;
    private String description;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private Campus campus;

    private String collageMajorName;

    // candidate1 정보
    private String candidate1Name;

    private String candidate1Nominee1Name;
    private String candidate1Nominee1StudentId;
    private String candidate1Nominee1College;
    private String candidate1Nominee1Department;
    private boolean candidate1Nominee1Main;
    private String candidate1Nominee1Description1;
    private String candidate1Nominee1Description2;
    private String candidate1Nominee1Description3;

    private String candidate1Nominee2Name;
    private String candidate1Nominee2StudentId;
    private String candidate1Nominee2College;
    private String candidate1Nominee2Department;
    private boolean candidate1Nominee2Main;
    private String candidate1Nominee2Description1;
    private String candidate1Nominee2Description2;
    private String candidate1Nominee2Description3;

    // candidate2 정보
    private String candidate2Name;

    private String candidate2Nominee1Name;
    private String candidate2Nominee1StudentId;
    private String candidate2Nominee1College;
    private String candidate2Nominee1Department;
    private boolean candidate2Nominee1Main;
    private String candidate2Nominee1Description1;
    private String candidate2Nominee1Description2;
    private String candidate2Nominee1Description3;

    private String candidate2Nominee2Name;
    private String candidate2Nominee2StudentId;
    private String candidate2Nominee2College;
    private String candidate2Nominee2Department;
    private boolean candidate2Nominee2Main;
    private String candidate2Nominee2Description1;
    private String candidate2Nominee2Description2;
    private String candidate2Nominee2Description3;
}
