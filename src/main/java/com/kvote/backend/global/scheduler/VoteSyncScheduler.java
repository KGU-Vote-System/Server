package com.kvote.backend.global.scheduler;

import com.kvote.backend.service.CandidateService;
import com.kvote.backend.domain.Election;
import com.kvote.backend.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteSyncScheduler {

    private final ElectionRepository electionRepository;
    private final CandidateService candidateService;

    @Scheduled(fixedRate = 60000) // 1분
    public void syncAllVoteCounts() {
        log.info("[스케줄러] 전체 선거 투표수 동기화 시작");

        List<Election> elections = electionRepository.findAll();
        for (Election election : elections) {
            try {
                candidateService.syncVoteCount(BigInteger.valueOf(election.getId()));
                log.info("[스케줄러] 동기화 완료 - 선거 ID: {}", election.getId());
            } catch (Exception e) {
                log.error("[스케줄러] 동기화 실패 - 선거 ID: {}", election.getId(), e);
            }
        }
    }
}