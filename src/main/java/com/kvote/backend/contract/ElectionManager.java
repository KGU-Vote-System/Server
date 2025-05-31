package com.kvote.backend.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class ElectionManager extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b50335f5f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506113d08061005b5f395ff3fe608060405234801561000f575f5ffd5b5060043610610086575f3560e01c80638da5cb5b116100595780638da5cb5b14610126578063997d2830146101445780639c98bcbb14610162578063b384abef1461017e57610086565b806315cf4a5f1461008a5780631750a3d0146100a65780634bd46448146100c25780635e6fef01146100f3575b5f5ffd5b6100a4600480360381019061009f9190610b04565b61019a565b005b6100c060048036038101906100bb9190610b7e565b6102eb565b005b6100dc60048036038101906100d79190610bd8565b61049b565b6040516100ea929190610c85565b60405180910390f35b61010d60048036038101906101089190610cb3565b6105ba565b60405161011d9493929190610cf8565b60405180910390f35b61012e610678565b60405161013b9190610d81565b60405180910390f35b61014c61069c565b6040516101599190610d9a565b60405180910390f35b61017c60048036038101906101779190610cb3565b6106a2565b005b61019860048036038101906101939190610bd8565b6107b3565b005b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610228576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161021f90610dfd565b60405180910390fd5b60015f81548092919061023a90610e48565b919050555060015460025f60015481526020019081526020015f205f01819055508060025f60015481526020019081526020015f20600101908161027e919061108c565b50600160025f60015481526020019081526020015f206005015f6101000a81548160ff0219169083151502179055507f52be7c4e77b4de76b7607d621492061fe13b58597e72dfb5e51ab8f6187ed141600154826040516102e092919061115b565b60405180910390a150565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610379576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161037090610dfd565b60405180910390fd5b5f60025f8481526020019081526020015f209050806005015f9054906101000a900460ff166103dd576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103d4906111d3565b60405180910390fd5b806002015f8154809291906103f190610e48565b91905055506040518060600160405280826002015481526020018381526020015f815250816003015f836002015481526020019081526020015f205f820151815f01556020820151816001019081610449919061108c565b50604082015181600201559050507fed8911b3df733b7d5f75724158e54478ea12e30f49c9d31b5261879f5b76586f8382600201548460405161048e939291906111f1565b60405180910390a1505050565b60605f5f60025f8681526020019081526020015f2090505f841180156104c5575080600201548411155b610504576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104fb90611277565b60405180910390fd5b5f816003015f8681526020019081526020015f20905080600101816002015481805461052f90610ebc565b80601f016020809104026020016040519081016040528092919081815260200182805461055b90610ebc565b80156105a65780601f1061057d576101008083540402835291602001916105a6565b820191905f5260205f20905b81548152906001019060200180831161058957829003601f168201915b505050505091509350935050509250929050565b6002602052805f5260405f205f91509050805f0154908060010180546105df90610ebc565b80601f016020809104026020016040519081016040528092919081815260200182805461060b90610ebc565b80156106565780601f1061062d57610100808354040283529160200191610656565b820191905f5260205f20905b81548152906001019060200180831161063957829003601f168201915b505050505090806002015490806005015f9054906101000a900460ff16905084565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60015481565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610730576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161072790610dfd565b60405180910390fd5b5f60025f8381526020019081526020015f209050806005015f9054906101000a900460ff16610794576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161078b906112df565b60405180910390fd5b5f816005015f6101000a81548160ff0219169083151502179055505050565b5f60025f8481526020019081526020015f209050806005015f9054906101000a900460ff16610817576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161080e906111d3565b60405180910390fd5b806004015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16156108a3576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161089a90611347565b60405180910390fd5b5f821180156108b6575080600201548211155b6108f5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108ec90611277565b60405180910390fd5b6001816004015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550806003015f8381526020019081526020015f206002015f81548092919061097290610e48565b91905055507fd91ecafccf238642dccd41161308b3eebe62a12bc5819daf5164780c221ec95b8383336040516109aa93929190611365565b60405180910390a1505050565b5f604051905090565b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b610a16826109d0565b810181811067ffffffffffffffff82111715610a3557610a346109e0565b5b80604052505050565b5f610a476109b7565b9050610a538282610a0d565b919050565b5f67ffffffffffffffff821115610a7257610a716109e0565b5b610a7b826109d0565b9050602081019050919050565b828183375f83830152505050565b5f610aa8610aa384610a58565b610a3e565b905082815260208101848484011115610ac457610ac36109cc565b5b610acf848285610a88565b509392505050565b5f82601f830112610aeb57610aea6109c8565b5b8135610afb848260208601610a96565b91505092915050565b5f60208284031215610b1957610b186109c0565b5b5f82013567ffffffffffffffff811115610b3657610b356109c4565b5b610b4284828501610ad7565b91505092915050565b5f819050919050565b610b5d81610b4b565b8114610b67575f5ffd5b50565b5f81359050610b7881610b54565b92915050565b5f5f60408385031215610b9457610b936109c0565b5b5f610ba185828601610b6a565b925050602083013567ffffffffffffffff811115610bc257610bc16109c4565b5b610bce85828601610ad7565b9150509250929050565b5f5f60408385031215610bee57610bed6109c0565b5b5f610bfb85828601610b6a565b9250506020610c0c85828601610b6a565b9150509250929050565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f610c4882610c16565b610c528185610c20565b9350610c62818560208601610c30565b610c6b816109d0565b840191505092915050565b610c7f81610b4b565b82525050565b5f6040820190508181035f830152610c9d8185610c3e565b9050610cac6020830184610c76565b9392505050565b5f60208284031215610cc857610cc76109c0565b5b5f610cd584828501610b6a565b91505092915050565b5f8115159050919050565b610cf281610cde565b82525050565b5f608082019050610d0b5f830187610c76565b8181036020830152610d1d8186610c3e565b9050610d2c6040830185610c76565b610d396060830184610ce9565b95945050505050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f610d6b82610d42565b9050919050565b610d7b81610d61565b82525050565b5f602082019050610d945f830184610d72565b92915050565b5f602082019050610dad5f830184610c76565b92915050565b7f4f6e6c79206f776e65722063616e20646f2074686973000000000000000000005f82015250565b5f610de7601683610c20565b9150610df282610db3565b602082019050919050565b5f6020820190508181035f830152610e1481610ddb565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610e5282610b4b565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203610e8457610e83610e1b565b5b600182019050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680610ed357607f821691505b602082108103610ee657610ee5610e8f565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302610f487fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610f0d565b610f528683610f0d565b95508019841693508086168417925050509392505050565b5f819050919050565b5f610f8d610f88610f8384610b4b565b610f6a565b610b4b565b9050919050565b5f819050919050565b610fa683610f73565b610fba610fb282610f94565b848454610f19565b825550505050565b5f5f905090565b610fd1610fc2565b610fdc818484610f9d565b505050565b5b81811015610fff57610ff45f82610fc9565b600181019050610fe2565b5050565b601f8211156110445761101581610eec565b61101e84610efe565b8101602085101561102d578190505b61104161103985610efe565b830182610fe1565b50505b505050565b5f82821c905092915050565b5f6110645f1984600802611049565b1980831691505092915050565b5f61107c8383611055565b9150826002028217905092915050565b61109582610c16565b67ffffffffffffffff8111156110ae576110ad6109e0565b5b6110b88254610ebc565b6110c3828285611003565b5f60209050601f8311600181146110f4575f84156110e2578287015190505b6110ec8582611071565b865550611153565b601f19841661110286610eec565b5f5b8281101561112957848901518255600182019150602085019450602081019050611104565b868310156111465784890151611142601f891682611055565b8355505b6001600288020188555050505b505050505050565b5f60408201905061116e5f830185610c76565b81810360208301526111808184610c3e565b90509392505050565b7f456c656374696f6e206e6f7420616374697665000000000000000000000000005f82015250565b5f6111bd601383610c20565b91506111c882611189565b602082019050919050565b5f6020820190508181035f8301526111ea816111b1565b9050919050565b5f6060820190506112045f830186610c76565b6112116020830185610c76565b81810360408301526112238184610c3e565b9050949350505050565b7f496e76616c69642063616e6469646174650000000000000000000000000000005f82015250565b5f611261601183610c20565b915061126c8261122d565b602082019050919050565b5f6020820190508181035f83015261128e81611255565b9050919050565b7f456c656374696f6e20616c726561647920656e646564000000000000000000005f82015250565b5f6112c9601683610c20565b91506112d482611295565b602082019050919050565b5f6020820190508181035f8301526112f6816112bd565b9050919050565b7f416c726561647920766f74656420696e207468697320656c656374696f6e00005f82015250565b5f611331601e83610c20565b915061133c826112fd565b602082019050919050565b5f6020820190508181035f83015261135e81611325565b9050919050565b5f6060820190506113785f830186610c76565b6113856020830185610c76565b6113926040830184610d72565b94935050505056fea26469706673582212205cc119dff617da4fa381549ebeb1f665e0d8c9b6a27b0777328da8ba7fe800f464736f6c634300081e0033";

    public static final String FUNC_ADDCANDIDATE = "addCandidate";

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ENDELECTION = "endElection";

    public static final String FUNC_GETCANDIDATE = "getCandidate";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_VOTE = "vote";

    public static final Event CANDIDATEADDED_EVENT = new Event("CandidateAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ELECTIONCREATED_EVENT = new Event("ElectionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTED_EVENT = new Event("Voted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected ElectionManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ElectionManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ElectionManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ElectionManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<CandidateAddedEventResponse> getCandidateAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CANDIDATEADDED_EVENT, transactionReceipt);
        ArrayList<CandidateAddedEventResponse> responses = new ArrayList<CandidateAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CandidateAddedEventResponse typedResponse = new CandidateAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CandidateAddedEventResponse> candidateAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CandidateAddedEventResponse>() {
            @Override
            public CandidateAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CANDIDATEADDED_EVENT, log);
                CandidateAddedEventResponse typedResponse = new CandidateAddedEventResponse();
                typedResponse.log = log;
                typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CandidateAddedEventResponse> candidateAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATEADDED_EVENT));
        return candidateAddedEventFlowable(filter);
    }

    public List<ElectionCreatedEventResponse> getElectionCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ELECTIONCREATED_EVENT, transactionReceipt);
        ArrayList<ElectionCreatedEventResponse> responses = new ArrayList<ElectionCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ElectionCreatedEventResponse typedResponse = new ElectionCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ElectionCreatedEventResponse> electionCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ElectionCreatedEventResponse>() {
            @Override
            public ElectionCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ELECTIONCREATED_EVENT, log);
                ElectionCreatedEventResponse typedResponse = new ElectionCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ElectionCreatedEventResponse> electionCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ELECTIONCREATED_EVENT));
        return electionCreatedEventFlowable(filter);
    }

    public List<VotedEventResponse> getVotedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTED_EVENT, transactionReceipt);
        ArrayList<VotedEventResponse> responses = new ArrayList<VotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotedEventResponse typedResponse = new VotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.voter = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VotedEventResponse> votedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VotedEventResponse>() {
            @Override
            public VotedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTED_EVENT, log);
                VotedEventResponse typedResponse = new VotedEventResponse();
                typedResponse.log = log;
                typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.voter = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VotedEventResponse> votedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTED_EVENT));
        return votedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addCandidate(BigInteger _electionId, String _name) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Utf8String(_name)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createElection(String _title) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_title)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> electionCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ELECTIONCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, String, BigInteger, Boolean>> elections(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ELECTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, String, BigInteger, Boolean>>(function,
                new Callable<Tuple4<BigInteger, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple4<BigInteger, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, String, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> endElection(BigInteger _electionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ENDELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> getCandidate(BigInteger _electionId, BigInteger _candidateId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _electionId, BigInteger _candidateId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ElectionManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ElectionManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ElectionManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ElectionManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ElectionManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ElectionManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ElectionManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ElectionManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ElectionManager> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ElectionManager.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ElectionManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ElectionManager.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ElectionManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ElectionManager.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ElectionManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ElectionManager.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class CandidateAddedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger candidateId;

        public String name;
    }

    public static class ElectionCreatedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public String title;
    }

    public static class VotedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger candidateId;

        public String voter;
    }
}
