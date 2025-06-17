package com.kvote.backend.domain;

import com.kvote.backend.dto.NomineeRequestDto;
import com.kvote.backend.dto.NomineeResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nominees")
@Builder
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean isMain; // true = 정후보, false = 부후보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    public void update(NomineeRequestDto nomineeRequestDto) {
        this.name = nomineeRequestDto.getName();
        this.studentId = nomineeRequestDto.getStudentId();
        this.college = nomineeRequestDto.getCollege();
        this.department = nomineeRequestDto.getDepartment();
        this.description = nomineeRequestDto.getDescription();
        this.isMain = nomineeRequestDto.isMain();
    }
}
