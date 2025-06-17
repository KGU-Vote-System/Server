package com.kvote.backend.global.handler;
import com.kvote.backend.config.BlockchainProperties;
import com.kvote.backend.domain.VoteAuditLog;
import com.kvote.backend.repository.VoteAuditLogRepository;
import com.kvote.backend.service.CandidateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class VoteEventListener {

    private static final Logger log = LoggerFactory.getLogger(VoteEventListener.class);

    private final Web3j web3j;
    private final VoteAuditLogRepository voteAuditLogRepository;
    private final BlockchainProperties blockchainProperties;

    public static final Event VOTED_EVENT = new Event("Voted",
            Arrays.asList(
                    TypeReference.create(Uint256.class), // indexed electionId
                    TypeReference.create(Address.class)  // indexed voter
            )
    );

    private final CandidateService candidateService;

//    @PostConstruct
//    public void startListening() {
//        String contractAddress = blockchainProperties.getContractAddress();
//
//        EthFilter filter = new EthFilter(
//                DefaultBlockParameterName.LATEST,
//                DefaultBlockParameterName.LATEST,
//                contractAddress
//        );
//
//        web3j.ethLogFlowable(filter).subscribe(eventLog -> {
//            try {
//                EventValues values = Contract.staticExtractEventParameters(VOTED_EVENT, eventLog);
//                if (values == null) {
//                    log.warn("⚠️ Could not extract event values from log: {}", eventLog);
//                    return;
//                }
//
//                BigInteger electionId = (BigInteger) values.getIndexedValues().get(0).getValue();
//                String voter = values.getIndexedValues().get(1).getValue().toString();
//
//                VoteAuditLog auditLog = VoteAuditLog.builder()
//                        .electionId(electionId.longValue())
//                        .voterAddress(voter)
//                        .txHash(eventLog.getTransactionHash())
//                        .timestamp(new Date())
//                        .build();
//
//                candidateService.syncVoteCount(electionId); // Update candidate vote count immediately
//                voteAuditLogRepository.save(auditLog);
//                log.info("✅ Voted event processed: electionId={}, voter={}", electionId, voter);
//            } catch (Exception e) {
//                log.error("❌ Error processing Voted event: {}", e.getMessage(), e);
//            }
//
//        }, error -> {
//            log.error("❌ Error subscribing to Voted events", error);
//        });
//
//        log.info("🎧 VoteEventListener started, listening for Voted events on contract: {}", contractAddress);
//    }
}