package com.kvote.backend.service;


import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Notice;
import com.kvote.backend.domain.NoticeStatus;
import com.kvote.backend.domain.NoticeType;
import com.kvote.backend.dto.NoticeRequestDto;
import com.kvote.backend.dto.NoticeDetailDto;
import com.kvote.backend.dto.NoticeListItemDto;
import com.kvote.backend.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeDetailDto creatNotice(NoticeRequestDto dto){

        Notice notice = Notice.from(dto);
        if (notice.getNoticeType() == NoticeType.NOTIFY) {
            notice.setNoticeStatus(NoticeStatus.NOTIFY);
        } else {
            notice.setNoticeStatus(notice.calculateStatus());
        }

        Notice saved = noticeRepository.save(notice);
        return NoticeDetailDto.from(saved);
    }

    public NoticeDetailDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 공지사항이 존재하지 않습니다."));
        return NoticeDetailDto.from(notice);
    }

    public List<NoticeListItemDto> getNoticesByCampus(Campus campus) {

        List<Campus> campuses = Arrays.asList(campus, Campus.ALL);

        List<Notice> notices = noticeRepository.findWithAlarmFirstByCampusIn(campuses);

        return notices.stream()
                .map(NoticeListItemDto::from)
                .collect(Collectors.toList());
    }




    @Transactional
    public NoticeDetailDto updateNotice(Long id, NoticeRequestDto dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("공지사항이 존재하지 않습니다."));

        notice.updateFromDto(dto);

        Notice savedNotice = noticeRepository.save(notice);

        return NoticeDetailDto.from(savedNotice);
    }


    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }
}
