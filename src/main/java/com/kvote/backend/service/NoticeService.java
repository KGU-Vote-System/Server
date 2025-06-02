package com.kvote.backend.service;


import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.Notice;
import com.kvote.backend.domain.NoticeStatus;
import com.kvote.backend.domain.NoticeType;
import com.kvote.backend.dto.NoticeRequestDto;
import com.kvote.backend.dto.NoticeResponseDto;
import com.kvote.backend.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeResponseDto creatNotice(NoticeRequestDto dto){

        Notice notice = Notice.from(dto);
        if (notice.getNoticeType() == NoticeType.NOTIFY) {
            notice.setNoticeStatus(NoticeStatus.NOTIFY);
        } else {
            notice.setNoticeStatus(notice.calculateStatus());  // 도메인 내부 메서드 호출
        }

        Notice saved = noticeRepository.save(notice);
        return NoticeResponseDto.from(saved);
    }

    public NoticeResponseDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 공지사항이 존재하지 않습니다."));
        return NoticeResponseDto.from(notice);
    }

    public List<NoticeResponseDto> getNoticesByCampus(Campus campus) {
        List<Notice> notices = noticeRepository.findByCampusOrderByStartAtDesc(campus);
        return notices.stream()
                .map(NoticeResponseDto::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public NoticeResponseDto updateNotice(Long id, NoticeRequestDto dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("공지사항이 존재하지 않습니다."));

        notice.updateFromDto(dto);

        Notice savedNotice = noticeRepository.save(notice);

        return NoticeResponseDto.from(savedNotice);
    }


    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }
}
