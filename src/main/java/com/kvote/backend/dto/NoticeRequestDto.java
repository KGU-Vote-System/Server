package com.kvote.backend.dto;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.NoticeStatus;
import com.kvote.backend.domain.NoticeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private NoticeType noticeType;
    // 알람 or 선거 구분

    @NotNull
    private Date createdAt;

    @NotNull
    private Campus campus;


    @NotNull
    private Date startAt;

    @NotNull
    private Date endAt;

}

