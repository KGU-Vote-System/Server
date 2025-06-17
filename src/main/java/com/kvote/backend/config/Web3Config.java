package com.kvote.backend.config;

import com.kvote.backend.contract.ElectionManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

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
    public ElectionManager electionManager(BlockchainProperties props, Web3j web3j, Credentials credentials) throws Exception {
        // 🔥 최신 블록의 baseFeePerGas 가져오기
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        BigInteger baseFee = block.getBlock().getBaseFeePerGas();

        // 🔧 baseFee + 1 Gwei tip
        BigInteger tip = BigInteger.valueOf(1_000_000_000L);
        BigInteger gasPrice = baseFee.add(tip);

        // 📌 가스 리밋은 여유 있게
        BigInteger gasLimit = BigInteger.valueOf(3_000_000L);

        ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);

        return ElectionManager.load(
                props.getContractAddress(),
                web3j,
                credentials,
                gasProvider
        );
    }
}