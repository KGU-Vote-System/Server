package com.kvote.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BlockchainProperties {

    @Value("${blockchain.rpc-url}")
    private String rpcUrl;

    @Value("${blockchain.private-key}")
    private String privateKey;

    @Value("${blockchain.contract-address}")
    private String contractAddress;
}