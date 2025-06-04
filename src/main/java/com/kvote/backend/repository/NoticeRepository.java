package com.kvote.backend.repository;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n " +
            "WHERE n.campus IN :campuses " +
            "ORDER BY CASE WHEN n.noticeType = 'ALARM' THEN 0 ELSE 1 END, n.startAt DESC")
    List<Notice> findWithAlarmFirstByCampusIn(@Param("campuses") List<Campus> campuses);
}
