package com.kvote.backend.repository;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByCampusOrderByStartAtDesc(Campus campus);
}
