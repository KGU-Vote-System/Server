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
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple5;
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
    public static final String BINARY = "6080604052348015600e575f5ffd5b50335f5f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611be88061005b5f395ff3fe608060405234801561000f575f5ffd5b50600436106100b2575f3560e01c80638da5cb5b1161006f5780638da5cb5b146101a0578063997d2830146101be5780639c98bcbb146101dc578063b384abef146101f8578063cf4a606014610214578063d1c6644b14610230576100b2565b806315cf4a5f146100b65780631750a3d0146100d25780632ce35e11146100ee5780634bd464481461011e5780635e6fef0114610150578063740c47d414610184575b5f5ffd5b6100d060048036038101906100cb9190611102565b61024c565b005b6100ec60048036038101906100e7919061117c565b6103cb565b005b610108600480360381019061010391906111d6565b6105bd565b6040516101159190611210565b60405180910390f35b61013860048036038101906101339190611229565b610621565b604051610147939291906112e1565b60405180910390f35b61016a600480360381019061016591906111d6565b610754565b60405161017b95949392919061131d565b60405180910390f35b61019e600480360381019061019991906111d6565b610825565b005b6101a8610971565b6040516101b591906113b4565b60405180910390f35b6101c6610995565b6040516101d39190611210565b60405180910390f35b6101f660048036038101906101f191906111d6565b61099b565b005b610212600480360381019061020d9190611229565b610aac565b005b61022e60048036038101906102299190611229565b610d2e565b005b61024a600480360381019061024591906113cd565b610e92565b005b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146102da576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102d190611483565b60405180910390fd5b60015f8154809291906102ec906114ce565b919050555060015460025f60015481526020019081526020015f205f01819055508060025f60015481526020019081526020015f2060010190816103309190611712565b50600160025f60015481526020019081526020015f206005015f6101000a81548160ff0219169083151502179055505f60025f60015481526020019081526020015f2060050160016101000a81548160ff0219169083151502179055507f52be7c4e77b4de76b7607d621492061fe13b58597e72dfb5e51ab8f6187ed141600154826040516103c09291906117e1565b60405180910390a150565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610459576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161045090611483565b60405180910390fd5b5f60025f8481526020019081526020015f209050806005015f9054906101000a900460ff16801561049957508060050160019054906101000a900460ff16155b6104d8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104cf90611859565b60405180910390fd5b806002015f8154809291906104ec906114ce565b91905055506040518060800160405280826002015481526020018381526020015f81526020015f1515815250816003015f836002015481526020019081526020015f205f820151815f0155602082015181600101908161054c9190611712565b50604082015181600201556060820151816003015f6101000a81548160ff0219169083151502179055509050507fed8911b3df733b7d5f75724158e54478ea12e30f49c9d31b5261879f5b76586f838260020154846040516105b093929190611877565b60405180910390a1505050565b5f5f60025f8481526020019081526020015f2090505f600190505b8160020154811161061a57816003015f8281526020019081526020015f20600201548361060591906118b3565b92508080610612906114ce565b9150506105d8565b5050919050565b60605f5f5f60025f8781526020019081526020015f2090505f8511801561064c575080600201548511155b61068b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161068290611930565b60405180910390fd5b5f816003015f8781526020019081526020015f209050806001018160020154826003015f9054906101000a900460ff168280546106c790611542565b80601f01602080910402602001604051908101604052809291908181526020018280546106f390611542565b801561073e5780601f106107155761010080835404028352916020019161073e565b820191905f5260205f20905b81548152906001019060200180831161072157829003601f168201915b5050505050925094509450945050509250925092565b6002602052805f5260405f205f91509050805f01549080600101805461077990611542565b80601f01602080910402602001604051908101604052809291908181526020018280546107a590611542565b80156107f05780601f106107c7576101008083540402835291602001916107f0565b820191905f5260205f20905b8154815290600101906020018083116107d357829003601f168201915b505050505090806002015490806005015f9054906101000a900460ff16908060050160019054906101000a900460ff16905085565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146108b3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108aa90611483565b60405180910390fd5b5f60025f8381526020019081526020015f2090508060050160019054906101000a900460ff1615610919576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161091090611998565b60405180910390fd5b60018160050160016101000a81548160ff0219169083151502179055507fbb78d5fa658dbdad95908d420646ea8bb64f50a9daa1acaa682effe6703f1ed7826040516109659190611210565b60405180910390a15050565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60015481565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610a29576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a2090611483565b60405180910390fd5b5f60025f8381526020019081526020015f209050806005015f9054906101000a900460ff16610a8d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a8490611a00565b60405180910390fd5b5f816005015f6101000a81548160ff0219169083151502179055505050565b5f60025f8481526020019081526020015f209050806005015f9054906101000a900460ff168015610aec57508060050160019054906101000a900460ff16155b610b2b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b2290611859565b60405180910390fd5b806004015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615610bb7576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bae90611a68565b60405180910390fd5b5f82118015610bca575080600201548211155b610c09576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c0090611930565b60405180910390fd5b806003015f8381526020019081526020015f206003015f9054906101000a900460ff1615610c6c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c6390611ad0565b60405180910390fd5b6001816004015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550806003015f8381526020019081526020015f206002015f815480929190610ce9906114ce565b91905055507fd91ecafccf238642dccd41161308b3eebe62a12bc5819daf5164780c221ec95b838333604051610d2193929190611aee565b60405180910390a1505050565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610dbc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610db390611483565b60405180910390fd5b5f60025f8481526020019081526020015f2090505f816003015f8481526020019081526020015f209050806003015f9054906101000a900460ff1615610e37576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e2e90611b6d565b60405180910390fd5b6001816003015f6101000a81548160ff0219169083151502179055507f48bf3ef4944e20b74a7ae7804d664a8e9ba28141c5fc26163c87f7fbc9e90e2f8484604051610e84929190611b8b565b60405180910390a150505050565b5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610f20576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f1790611483565b60405180910390fd5b5f60025f8581526020019081526020015f2090508060050160019054906101000a900460ff1615610f86576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f7d90611998565b60405180910390fd5b5f816003015f8581526020019081526020015f20905082816001019081610fad9190611712565b505050505050565b5f604051905090565b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b61101482610fce565b810181811067ffffffffffffffff8211171561103357611032610fde565b5b80604052505050565b5f611045610fb5565b9050611051828261100b565b919050565b5f67ffffffffffffffff8211156110705761106f610fde565b5b61107982610fce565b9050602081019050919050565b828183375f83830152505050565b5f6110a66110a184611056565b61103c565b9050828152602081018484840111156110c2576110c1610fca565b5b6110cd848285611086565b509392505050565b5f82601f8301126110e9576110e8610fc6565b5b81356110f9848260208601611094565b91505092915050565b5f6020828403121561111757611116610fbe565b5b5f82013567ffffffffffffffff81111561113457611133610fc2565b5b611140848285016110d5565b91505092915050565b5f819050919050565b61115b81611149565b8114611165575f5ffd5b50565b5f8135905061117681611152565b92915050565b5f5f6040838503121561119257611191610fbe565b5b5f61119f85828601611168565b925050602083013567ffffffffffffffff8111156111c0576111bf610fc2565b5b6111cc858286016110d5565b9150509250929050565b5f602082840312156111eb576111ea610fbe565b5b5f6111f884828501611168565b91505092915050565b61120a81611149565b82525050565b5f6020820190506112235f830184611201565b92915050565b5f5f6040838503121561123f5761123e610fbe565b5b5f61124c85828601611168565b925050602061125d85828601611168565b9150509250929050565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f61129982611267565b6112a38185611271565b93506112b3818560208601611281565b6112bc81610fce565b840191505092915050565b5f8115159050919050565b6112db816112c7565b82525050565b5f6060820190508181035f8301526112f9818661128f565b90506113086020830185611201565b61131560408301846112d2565b949350505050565b5f60a0820190506113305f830188611201565b8181036020830152611342818761128f565b90506113516040830186611201565b61135e60608301856112d2565b61136b60808301846112d2565b9695505050505050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f61139e82611375565b9050919050565b6113ae81611394565b82525050565b5f6020820190506113c75f8301846113a5565b92915050565b5f5f5f606084860312156113e4576113e3610fbe565b5b5f6113f186828701611168565b935050602061140286828701611168565b925050604084013567ffffffffffffffff81111561142357611422610fc2565b5b61142f868287016110d5565b9150509250925092565b7f4f6e6c79206f776e65722063616e20646f2074686973000000000000000000005f82015250565b5f61146d601683611271565b915061147882611439565b602082019050919050565b5f6020820190508181035f83015261149a81611461565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6114d882611149565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff820361150a576115096114a1565b5b600182019050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061155957607f821691505b60208210810361156c5761156b611515565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026115ce7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611593565b6115d88683611593565b95508019841693508086168417925050509392505050565b5f819050919050565b5f61161361160e61160984611149565b6115f0565b611149565b9050919050565b5f819050919050565b61162c836115f9565b6116406116388261161a565b84845461159f565b825550505050565b5f5f905090565b611657611648565b611662818484611623565b505050565b5b818110156116855761167a5f8261164f565b600181019050611668565b5050565b601f8211156116ca5761169b81611572565b6116a484611584565b810160208510156116b3578190505b6116c76116bf85611584565b830182611667565b50505b505050565b5f82821c905092915050565b5f6116ea5f19846008026116cf565b1980831691505092915050565b5f61170283836116db565b9150826002028217905092915050565b61171b82611267565b67ffffffffffffffff81111561173457611733610fde565b5b61173e8254611542565b611749828285611689565b5f60209050601f83116001811461177a575f8415611768578287015190505b61177285826116f7565b8655506117d9565b601f19841661178886611572565b5f5b828110156117af5784890151825560018201915060208501945060208101905061178a565b868310156117cc57848901516117c8601f8916826116db565b8355505b6001600288020188555050505b505050505050565b5f6040820190506117f45f830185611201565b8181036020830152611806818461128f565b90509392505050565b7f456c656374696f6e206e6f7420616374697665206f722064656c6574656400005f82015250565b5f611843601e83611271565b915061184e8261180f565b602082019050919050565b5f6020820190508181035f83015261187081611837565b9050919050565b5f60608201905061188a5f830186611201565b6118976020830185611201565b81810360408301526118a9818461128f565b9050949350505050565b5f6118bd82611149565b91506118c883611149565b92508282019050808211156118e0576118df6114a1565b5b92915050565b7f496e76616c69642063616e6469646174650000000000000000000000000000005f82015250565b5f61191a601183611271565b9150611925826118e6565b602082019050919050565b5f6020820190508181035f8301526119478161190e565b9050919050565b7f456c656374696f6e20616c72656164792064656c6574656400000000000000005f82015250565b5f611982601883611271565b915061198d8261194e565b602082019050919050565b5f6020820190508181035f8301526119af81611976565b9050919050565b7f456c656374696f6e20616c726561647920656e646564000000000000000000005f82015250565b5f6119ea601683611271565b91506119f5826119b6565b602082019050919050565b5f6020820190508181035f830152611a17816119de565b9050919050565b7f416c726561647920766f74656420696e207468697320656c656374696f6e00005f82015250565b5f611a52601e83611271565b9150611a5d82611a1e565b602082019050919050565b5f6020820190508181035f830152611a7f81611a46565b9050919050565b7f43616e6469646174652069732064656c657465640000000000000000000000005f82015250565b5f611aba601483611271565b9150611ac582611a86565b602082019050919050565b5f6020820190508181035f830152611ae781611aae565b9050919050565b5f606082019050611b015f830186611201565b611b0e6020830185611201565b611b1b60408301846113a5565b949350505050565b7f43616e64696461746520616c72656164792064656c65746564000000000000005f82015250565b5f611b57601983611271565b9150611b6282611b23565b602082019050919050565b5f6020820190508181035f830152611b8481611b4b565b9050919050565b5f604082019050611b9e5f830185611201565b611bab6020830184611201565b939250505056fea2646970667358221220d4805901742ae31c56a0b1526d2efd16b27ddca6fe3c4e6c638a9f4a370c979964736f6c634300081e0033";

    public static final String FUNC_ADDCANDIDATE = "addCandidate";

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_DELETECANDIDATE = "deleteCandidate";

    public static final String FUNC_DELETEELECTION = "deleteElection";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ENDELECTION = "endElection";

    public static final String FUNC_GETCANDIDATE = "getCandidate";

    public static final String FUNC_GETTOTALVOTES = "getTotalVotes";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_UPDATECANDIDATENAME = "updateCandidateName";

    public static final String FUNC_VOTE = "vote";

    public static final Event CANDIDATEADDED_EVENT = new Event("CandidateAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CANDIDATEDELETED_EVENT = new Event("CandidateDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ELECTIONCREATED_EVENT = new Event("ElectionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ELECTIONDELETED_EVENT = new Event("ElectionDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
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

    public List<CandidateDeletedEventResponse> getCandidateDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CANDIDATEDELETED_EVENT, transactionReceipt);
        ArrayList<CandidateDeletedEventResponse> responses = new ArrayList<CandidateDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CandidateDeletedEventResponse typedResponse = new CandidateDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CandidateDeletedEventResponse> candidateDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CandidateDeletedEventResponse>() {
            @Override
            public CandidateDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CANDIDATEDELETED_EVENT, log);
                CandidateDeletedEventResponse typedResponse = new CandidateDeletedEventResponse();
                typedResponse.log = log;
                typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CandidateDeletedEventResponse> candidateDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATEDELETED_EVENT));
        return candidateDeletedEventFlowable(filter);
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

    public List<ElectionDeletedEventResponse> getElectionDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ELECTIONDELETED_EVENT, transactionReceipt);
        ArrayList<ElectionDeletedEventResponse> responses = new ArrayList<ElectionDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ElectionDeletedEventResponse typedResponse = new ElectionDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ElectionDeletedEventResponse> electionDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ElectionDeletedEventResponse>() {
            @Override
            public ElectionDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ELECTIONDELETED_EVENT, log);
                ElectionDeletedEventResponse typedResponse = new ElectionDeletedEventResponse();
                typedResponse.log = log;
                typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ElectionDeletedEventResponse> electionDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ELECTIONDELETED_EVENT));
        return electionDeletedEventFlowable(filter);
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

    public RemoteFunctionCall<TransactionReceipt> deleteCandidate(BigInteger _electionId, BigInteger _candidateId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETECANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteElection(BigInteger _electionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> electionCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ELECTIONCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, String, BigInteger, Boolean, Boolean>> elections(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ELECTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, String, BigInteger, Boolean, Boolean>>(function,
                new Callable<Tuple5<BigInteger, String, BigInteger, Boolean, Boolean>>() {
                    @Override
                    public Tuple5<BigInteger, String, BigInteger, Boolean, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, BigInteger, Boolean, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
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

    public RemoteFunctionCall<Tuple3<String, BigInteger, Boolean>> getCandidate(BigInteger _electionId, BigInteger _candidateId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, Boolean>>(function,
                new Callable<Tuple3<String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple3<String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getTotalVotes(BigInteger _electionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTOTALVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateCandidateName(BigInteger _electionId, BigInteger _candidateId, String _newName) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATECANDIDATENAME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId), 
                new org.web3j.abi.datatypes.Utf8String(_newName)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static class CandidateDeletedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger candidateId;
    }

    public static class ElectionCreatedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public String title;
    }

    public static class ElectionDeletedEventResponse extends BaseEventResponse {
        public BigInteger electionId;
    }

    public static class VotedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger candidateId;

        public String voter;
    }
}
