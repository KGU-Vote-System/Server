package com.kvote.backend.config;

import com.kvote.backend.contract.ElectionManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@Configuration
public class Web3Config {

    @Bean
    public Web3j web3j(BlockchainProperties props) {
        return Web3j.build(new HttpService(props.getRpcUrl()));
    }

    @Bean
    public Credentials credentials(BlockchainProperties props) {
        return Credentials.create(props.getPrivateKey());
    }

    @Bean
    public ElectionManager electionManager(BlockchainProperties props, Web3j web3j, Credentials credentials) {
        return ElectionManager.load(props.getContractAddress(), web3j, credentials, new org.web3j.tx.gas.DefaultGasProvider());
    }
}