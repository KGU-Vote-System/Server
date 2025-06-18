package com.kvote.backend.global.scheduler;

import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.service.ElectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElectionScheduler {

    private final ElectionManager electionManager;
    private final ElectionService electionService;

    @Scheduled(cron = "0 0 0 * * *") // 자정마다 실행
    public void scheduleElectionTasks() {
        // 여기에 선거 관련 스케줄링 작업을 구현합니다.
        electionService.scheduleEndElection();
    }
}
