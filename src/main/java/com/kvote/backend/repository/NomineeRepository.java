package com.kvote.backend.repository;

import com.kvote.backend.domain.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {
    List<Nominee> findAllByCandidateId(Long candidateId);

    Nominee findNomineeByCandidateId(Long candidateId);

    // Nominee 엔티티에 대한 CRUD 메서드가 JpaRepository에 정의되어 있습니다.
    // 추가적인 쿼리 메서드가 필요하다면 여기에 정의할 수 있습니다.

    // 예시: 특정 캠퍼스의 후보자 조회
    // List<Nominee> findByCampus(Campus campus);

}
