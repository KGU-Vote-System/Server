package com.kvote.backend.service;

import com.kvote.backend.domain.ElectionDraft;
import com.kvote.backend.dto.ElectionAllRequestDto;
import com.kvote.backend.repository.ElectionDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionDraftService {

    private final ElectionDraftRepository repository;

    public ElectionAllRequestDto saveAndReturnAsRequestDto(ElectionAllRequestDto dto) {
        ElectionDraft saved = repository.save(toEntity(dto));
        return toRequestDto(saved);
    }

    public ElectionAllRequestDto findAndDeleteByIdAsRequestDto(Long id) {
        ElectionDraft draft = findById(id);
        repository.deleteById(id);
        return toRequestDto(draft);
    }

    public ElectionAllRequestDto updateAndReturnAsRequestDto(Long id, ElectionAllRequestDto dto) {
        ElectionDraft draft = toEntity(dto);
        draft.setId(id);
        return toRequestDto(repository.save(draft));
    }

    public List<ElectionDraft> findAll() {
        return repository.findAll();
    }

    public ElectionDraft findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("임시저장 항목이 존재하지 않습니다."));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 변환: DTO → Entity
    private ElectionDraft toEntity(ElectionAllRequestDto dto) {
        return ElectionDraft.builder()
                .title(dto.getElection().getTitle())
                .description(dto.getElection().getDescription())
                .startAt(dto.getElection().getStartAt())
                .endAt(dto.getElection().getEndAt())
                .campus(dto.getElection().getCampus())
                .collageMajorName(dto.getElection().getCollageMajorName())

                .candidate1Name(dto.getCandidate1().getInfo().getName())
                .candidate1Nominee1Name(dto.getCandidate1().getNominee1().getName())
                .candidate1Nominee1StudentId(dto.getCandidate1().getNominee1().getStudentId())
                .candidate1Nominee1College(dto.getCandidate1().getNominee1().getCollege())
                .candidate1Nominee1Department(dto.getCandidate1().getNominee1().getDepartment())
                .candidate1Nominee1Main(dto.getCandidate1().getNominee1().isMain())
                .candidate1Nominee1Description1(dto.getCandidate1().getNominee1().getDescription1())
                .candidate1Nominee1Description2(dto.getCandidate1().getNominee1().getDescription2())
                .candidate1Nominee1Description3(dto.getCandidate1().getNominee1().getDescription3())

                .candidate1Nominee2Name(dto.getCandidate1().getNominee2().getName())
                .candidate1Nominee2StudentId(dto.getCandidate1().getNominee2().getStudentId())
                .candidate1Nominee2College(dto.getCandidate1().getNominee2().getCollege())
                .candidate1Nominee2Department(dto.getCandidate1().getNominee2().getDepartment())
                .candidate1Nominee2Main(dto.getCandidate1().getNominee2().isMain())
                .candidate1Nominee2Description1(dto.getCandidate1().getNominee2().getDescription1())
                .candidate1Nominee2Description2(dto.getCandidate1().getNominee2().getDescription2())
                .candidate1Nominee2Description3(dto.getCandidate1().getNominee2().getDescription3())

                .candidate2Name(dto.getCandidate2().getInfo().getName())
                .candidate2Nominee1Name(dto.getCandidate2().getNominee1().getName())
                .candidate2Nominee1StudentId(dto.getCandidate2().getNominee1().getStudentId())
                .candidate2Nominee1College(dto.getCandidate2().getNominee1().getCollege())
                .candidate2Nominee1Department(dto.getCandidate2().getNominee1().getDepartment())
                .candidate2Nominee1Main(dto.getCandidate2().getNominee1().isMain())
                .candidate2Nominee1Description1(dto.getCandidate2().getNominee1().getDescription1())
                .candidate2Nominee1Description2(dto.getCandidate2().getNominee1().getDescription2())
                .candidate2Nominee1Description3(dto.getCandidate2().getNominee1().getDescription3())

                .candidate2Nominee2Name(dto.getCandidate2().getNominee2().getName())
                .candidate2Nominee2StudentId(dto.getCandidate2().getNominee2().getStudentId())
                .candidate2Nominee2College(dto.getCandidate2().getNominee2().getCollege())
                .candidate2Nominee2Department(dto.getCandidate2().getNominee2().getDepartment())
                .candidate2Nominee2Main(dto.getCandidate2().getNominee2().isMain())
                .candidate2Nominee2Description1(dto.getCandidate2().getNominee2().getDescription1())
                .candidate2Nominee2Description2(dto.getCandidate2().getNominee2().getDescription2())
                .candidate2Nominee2Description3(dto.getCandidate2().getNominee2().getDescription3())
                .build();
    }

    // 변환: Entity → DTO
    public ElectionAllRequestDto toRequestDto(ElectionDraft draft) {
        return ElectionAllRequestDto.builder()
                .election(ElectionAllRequestDto.ElectionDto.builder()
                        .title(draft.getTitle())
                        .description(draft.getDescription())
                        .startAt(draft.getStartAt())
                        .endAt(draft.getEndAt())
                        .campus(draft.getCampus())
                        .collageMajorName(draft.getCollageMajorName())
                        .build()
                )
                .candidate1(ElectionAllRequestDto.CandidateDto.builder()
                        .info(ElectionAllRequestDto.CandidateDto.Info.builder()
                                .name(draft.getCandidate1Name())
                                .electionId(null) // 임시저장 상태이므로 electionId는 없음
                                .build()
                        )
                        .nominee1(ElectionAllRequestDto.CandidateDto.NomineeDto.builder()
                                .name(draft.getCandidate1Nominee1Name())
                                .studentId(draft.getCandidate1Nominee1StudentId())
                                .college(draft.getCandidate1Nominee1College())
                                .department(draft.getCandidate1Nominee1Department())
                                .main(draft.isCandidate1Nominee1Main())
                                .description1(draft.getCandidate1Nominee1Description1())
                                .description2(draft.getCandidate1Nominee1Description2())
                                .description3(draft.getCandidate1Nominee1Description3())
                                .build()
                        )
                        .nominee2(ElectionAllRequestDto.CandidateDto.NomineeDto.builder()
                                .name(draft.getCandidate1Nominee2Name())
                                .studentId(draft.getCandidate1Nominee2StudentId())
                                .college(draft.getCandidate1Nominee2College())
                                .department(draft.getCandidate1Nominee2Department())
                                .main(draft.isCandidate1Nominee2Main())
                                .description1(draft.getCandidate1Nominee2Description1())
                                .description2(draft.getCandidate1Nominee2Description2())
                                .description3(draft.getCandidate1Nominee2Description3())
                                .build()
                        )
                        .build()
                )
                .candidate2(ElectionAllRequestDto.CandidateDto.builder()
                        .info(ElectionAllRequestDto.CandidateDto.Info.builder()
                                .name(draft.getCandidate2Name())
                                .electionId(null)
                                .build()
                        )
                        .nominee1(ElectionAllRequestDto.CandidateDto.NomineeDto.builder()
                                .name(draft.getCandidate2Nominee1Name())
                                .studentId(draft.getCandidate2Nominee1StudentId())
                                .college(draft.getCandidate2Nominee1College())
                                .department(draft.getCandidate2Nominee1Department())
                                .main(draft.isCandidate2Nominee1Main())
                                .description1(draft.getCandidate2Nominee1Description1())
                                .description2(draft.getCandidate2Nominee1Description2())
                                .description3(draft.getCandidate2Nominee1Description3())
                                .build()
                        )
                        .nominee2(ElectionAllRequestDto.CandidateDto.NomineeDto.builder()
                                .name(draft.getCandidate2Nominee2Name())
                                .studentId(draft.getCandidate2Nominee2StudentId())
                                .college(draft.getCandidate2Nominee2College())
                                .department(draft.getCandidate2Nominee2Department())
                                .main(draft.isCandidate2Nominee2Main())
                                .description1(draft.getCandidate2Nominee2Description1())
                                .description2(draft.getCandidate2Nominee2Description2())
                                .description3(draft.getCandidate2Nominee2Description3())
                                .build()
                        )
                        .build()
                )
                .build();
    }

}
