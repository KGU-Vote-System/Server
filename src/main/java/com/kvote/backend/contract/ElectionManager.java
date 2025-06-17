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
    public static final String BINARY = "60806040525f6003553480156012575f5ffd5b503360015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550612526806100605f395ff3fe608060405234801561000f575f5ffd5b50600436106100f3575f3560e01c8063740c47d411610095578063b384abef11610064578063b384abef14610289578063cf4a6060146102a5578063d16ff4c8146102c1578063d1c6644b146102df576100f3565b8063740c47d4146102155780638da5cb5b14610231578063997d28301461024f5780639c98bcbb1461026d576100f3565b806335b8e820116100d157806335b8e82014610177578063432cf2ce146101a95780635e6fef01146101c5578063659cf277146101f9576100f3565b80631750a3d0146100f75780632ce35e11146101135780633477ee2e14610143575b5f5ffd5b610111600480360381019061010c9190611782565b6102fb565b005b61012d600480360381019061012891906117dc565b61051d565b60405161013a9190611816565b60405180910390f35b61015d600480360381019061015891906117dc565b61060b565b60405161016e9594939291906118a9565b60405180910390f35b610191600480360381019061018c91906117dc565b6106ce565b6040516101a093929190611901565b60405180910390f35b6101c360048036038101906101be919061193d565b6107e8565b005b6101df60048036038101906101da91906117dc565b6109c6565b6040516101f0959493929190611997565b60405180910390f35b610213600480360381019061020e9190611a49565b610a97565b005b61022f600480360381019061022a91906117dc565b610be8565b005b610239610da7565b6040516102469190611a96565b60405180910390f35b610257610dcc565b6040516102649190611816565b60405180910390f35b610287600480360381019061028291906117dc565b610dd2565b005b6102a3600480360381019061029e9190611aaf565b610ee4565b005b6102bf60048036038101906102ba9190611aaf565b611225565b005b6102c9611444565b6040516102d69190611816565b60405180910390f35b6102f960048036038101906102f49190611aed565b61144a565b005b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461038a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161038190611ba3565b60405180910390fd5b5f60045f8481526020019081526020015f209050806005015f9054906101000a900460ff1680156103ca57508060050160019054906101000a900460ff16155b610409576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161040090611c0b565b60405180910390fd5b60035f81548092919061041b90611c56565b91905055506040518060a0016040528060035481526020018481526020018381526020015f81526020015f15158152505f5f60035481526020019081526020015f205f820151815f01556020820151816001015560408201518160020190816104849190611e9a565b50606082015181600301556080820151816004015f6101000a81548160ff02191690831515021790555090505080600201600354908060018154018082558091505060019003905f5260205f20015f90919091909150557fed8911b3df733b7d5f75724158e54478ea12e30f49c9d31b5261879f5b76586f836003548460405161051093929190611f69565b60405180910390a1505050565b5f5f60045f8481526020019081526020015f2090508060050160019054906101000a900460ff1615610584576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161057b90611fef565b60405180910390fd5b5f5f90505b8160020180549050811015610604575f8260020182815481106105af576105ae61200d565b5b905f5260205f20015490505f5f5f8381526020019081526020015f209050806004015f9054906101000a900460ff166105f5578060030154856105f2919061203a565b94505b50508080600101915050610589565b5050919050565b5f602052805f5260405f205f91509050805f01549080600101549080600201805461063590611cca565b80601f016020809104026020016040519081016040528092919081815260200182805461066190611cca565b80156106ac5780601f10610683576101008083540402835291602001916106ac565b820191905f5260205f20905b81548152906001019060200180831161068f57829003601f168201915b505050505090806003015490806004015f9054906101000a900460ff16905085565b60605f5f5f5f5f8681526020019081526020015f209050806004015f9054906101000a900460ff1615610736576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161072d906120b7565b60405180910390fd5b806002018160030154826004015f9054906101000a900460ff1682805461075c90611cca565b80601f016020809104026020016040519081016040528092919081815260200182805461078890611cca565b80156107d35780601f106107aa576101008083540402835291602001916107d3565b820191905f5260205f20905b8154815290600101906020018083116107b657829003601f168201915b50505050509250935093509350509193909250565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610877576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161086e90611ba3565b60405180910390fd5b4281116108b9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108b09061211f565b60405180910390fd5b60025f8154809291906108cb90611c56565b919050555060025460045f60025481526020019081526020015f205f01819055508160045f60025481526020019081526020015f20600101908161090f9190611e9a565b50600160045f60025481526020019081526020015f206005015f6101000a81548160ff0219169083151502179055505f60045f60025481526020019081526020015f2060050160016101000a81548160ff0219169083151502179055508060045f60025481526020019081526020015f20600601819055507f52be7c4e77b4de76b7607d621492061fe13b58597e72dfb5e51ab8f6187ed141600254836040516109ba92919061213d565b60405180910390a15050565b6004602052805f5260405f205f91509050805f0154908060010180546109eb90611cca565b80601f0160208091040260200160405190810160405280929190818152602001828054610a1790611cca565b8015610a625780601f10610a3957610100808354040283529160200191610a62565b820191905f5260205f20905b815481529060010190602001808311610a4557829003601f168201915b505050505090806005015f9054906101000a900460ff16908060050160019054906101000a900460ff16908060060154905085565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610b26576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b1d90611ba3565b60405180910390fd5b5f60045f8481526020019081526020015f2090508060050160019054906101000a900460ff1615610b8c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b8390611fef565b60405180910390fd5b6001816004015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550505050565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610c77576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c6e90611ba3565b60405180910390fd5b5f60045f8381526020019081526020015f2090508060050160019054906101000a900460ff1615610cdd576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610cd490611fef565b60405180910390fd5b60018160050160016101000a81548160ff0219169083151502179055505f8160020190505f5f90505b8180549050811015610d6a575f828281548110610d2657610d2561200d565b5b905f5260205f200154905060015f5f8381526020019081526020015f206004015f6101000a81548160ff021916908315150217905550508080600101915050610d06565b507fbb78d5fa658dbdad95908d420646ea8bb64f50a9daa1acaa682effe6703f1ed783604051610d9a9190611816565b60405180910390a1505050565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60025481565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610e61576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e5890611ba3565b60405180910390fd5b5f60045f8381526020019081526020015f209050806005015f9054906101000a900460ff16610ec5576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ebc906121b5565b60405180910390fd5b5f816005015f6101000a81548160ff0219169083151502179055505050565b5f60045f8481526020019081526020015f209050806005015f9054906101000a900460ff168015610f2457508060050160019054906101000a900460ff16155b610f63576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f5a90611c0b565b60405180910390fd5b8060060154421115610faa576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610fa19061221d565b60405180910390fd5b806003015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615611036576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161102d90612285565b60405180910390fd5b806004015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff166110c1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016110b8906122ed565b60405180910390fd5b5f5f5f8481526020019081526020015f2090508381600101541461111a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016111119061237b565b60405180910390fd5b806004015f9054906101000a900460ff161561116b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401611162906120b7565b60405180910390fd5b6001826003015f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550806003015f8154809291906111d690611c56565b91905055503373ffffffffffffffffffffffffffffffffffffffff16847f030b0f8dcd86a031eddb071f91882edeac8173663ba775713b677b42b51be44b60405160405180910390a350505050565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146112b4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016112ab90611ba3565b60405180910390fd5b5f5f5f8381526020019081526020015f2090505f60045f8581526020019081526020015f209050816004015f9054906101000a900460ff161561132c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401611323906123e3565b60405180910390fd5b6001826004015f6101000a81548160ff0219169083151502179055505f8160020190505f5f90505b818054905081101561140357848282815481106113745761137361200d565b5b905f5260205f200154036113f65781600183805490506113949190612401565b815481106113a5576113a461200d565b5b905f5260205f2001548282815481106113c1576113c061200d565b5b905f5260205f200181905550818054806113de576113dd612434565b5b600190038181905f5260205f20015f90559055611403565b8080600101915050611354565b507f48bf3ef4944e20b74a7ae7804d664a8e9ba28141c5fc26163c87f7fbc9e90e2f8585604051611435929190612461565b60405180910390a15050505050565b60035481565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146114d9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016114d090611ba3565b60405180910390fd5b5f60045f8581526020019081526020015f2090508060050160019054906101000a900460ff161561153f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161153690611fef565b60405180910390fd5b5f5f5f8581526020019081526020015f209050806004015f9054906101000a900460ff16156115a3576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161159a906123e3565b60405180910390fd5b848160010154146115e9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016115e0906124d2565b60405180910390fd5b828160020190816115fa9190611e9a565b505050505050565b5f604051905090565b5f5ffd5b5f5ffd5b5f819050919050565b61162581611613565b811461162f575f5ffd5b50565b5f813590506116408161161c565b92915050565b5f5ffd5b5f5ffd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b6116948261164e565b810181811067ffffffffffffffff821117156116b3576116b261165e565b5b80604052505050565b5f6116c5611602565b90506116d1828261168b565b919050565b5f67ffffffffffffffff8211156116f0576116ef61165e565b5b6116f98261164e565b9050602081019050919050565b828183375f83830152505050565b5f611726611721846116d6565b6116bc565b9050828152602081018484840111156117425761174161164a565b5b61174d848285611706565b509392505050565b5f82601f83011261176957611768611646565b5b8135611779848260208601611714565b91505092915050565b5f5f604083850312156117985761179761160b565b5b5f6117a585828601611632565b925050602083013567ffffffffffffffff8111156117c6576117c561160f565b5b6117d285828601611755565b9150509250929050565b5f602082840312156117f1576117f061160b565b5b5f6117fe84828501611632565b91505092915050565b61181081611613565b82525050565b5f6020820190506118295f830184611807565b92915050565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f6118618261182f565b61186b8185611839565b935061187b818560208601611849565b6118848161164e565b840191505092915050565b5f8115159050919050565b6118a38161188f565b82525050565b5f60a0820190506118bc5f830188611807565b6118c96020830187611807565b81810360408301526118db8186611857565b90506118ea6060830185611807565b6118f7608083018461189a565b9695505050505050565b5f6060820190508181035f8301526119198186611857565b90506119286020830185611807565b611935604083018461189a565b949350505050565b5f5f604083850312156119535761195261160b565b5b5f83013567ffffffffffffffff8111156119705761196f61160f565b5b61197c85828601611755565b925050602061198d85828601611632565b9150509250929050565b5f60a0820190506119aa5f830188611807565b81810360208301526119bc8187611857565b90506119cb604083018661189a565b6119d8606083018561189a565b6119e56080830184611807565b9695505050505050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f611a18826119ef565b9050919050565b611a2881611a0e565b8114611a32575f5ffd5b50565b5f81359050611a4381611a1f565b92915050565b5f5f60408385031215611a5f57611a5e61160b565b5b5f611a6c85828601611632565b9250506020611a7d85828601611a35565b9150509250929050565b611a9081611a0e565b82525050565b5f602082019050611aa95f830184611a87565b92915050565b5f5f60408385031215611ac557611ac461160b565b5b5f611ad285828601611632565b9250506020611ae385828601611632565b9150509250929050565b5f5f5f60608486031215611b0457611b0361160b565b5b5f611b1186828701611632565b9350506020611b2286828701611632565b925050604084013567ffffffffffffffff811115611b4357611b4261160f565b5b611b4f86828701611755565b9150509250925092565b7f4f6e6c79206f776e65722063616e20646f2074686973000000000000000000005f82015250565b5f611b8d601683611839565b9150611b9882611b59565b602082019050919050565b5f6020820190508181035f830152611bba81611b81565b9050919050565b7f456c656374696f6e206e6f7420616374697665206f722064656c6574656400005f82015250565b5f611bf5601e83611839565b9150611c0082611bc1565b602082019050919050565b5f6020820190508181035f830152611c2281611be9565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f611c6082611613565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611c9257611c91611c29565b5b600182019050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680611ce157607f821691505b602082108103611cf457611cf3611c9d565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302611d567fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611d1b565b611d608683611d1b565b95508019841693508086168417925050509392505050565b5f819050919050565b5f611d9b611d96611d9184611613565b611d78565b611613565b9050919050565b5f819050919050565b611db483611d81565b611dc8611dc082611da2565b848454611d27565b825550505050565b5f5f905090565b611ddf611dd0565b611dea818484611dab565b505050565b5b81811015611e0d57611e025f82611dd7565b600181019050611df0565b5050565b601f821115611e5257611e2381611cfa565b611e2c84611d0c565b81016020851015611e3b578190505b611e4f611e4785611d0c565b830182611def565b50505b505050565b5f82821c905092915050565b5f611e725f1984600802611e57565b1980831691505092915050565b5f611e8a8383611e63565b9150826002028217905092915050565b611ea38261182f565b67ffffffffffffffff811115611ebc57611ebb61165e565b5b611ec68254611cca565b611ed1828285611e11565b5f60209050601f831160018114611f02575f8415611ef0578287015190505b611efa8582611e7f565b865550611f61565b601f198416611f1086611cfa565b5f5b82811015611f3757848901518255600182019150602085019450602081019050611f12565b86831015611f545784890151611f50601f891682611e63565b8355505b6001600288020188555050505b505050505050565b5f606082019050611f7c5f830186611807565b611f896020830185611807565b8181036040830152611f9b8184611857565b9050949350505050565b7f456c656374696f6e20616c72656164792064656c6574656400000000000000005f82015250565b5f611fd9601883611839565b9150611fe482611fa5565b602082019050919050565b5f6020820190508181035f83015261200681611fcd565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b5f61204482611613565b915061204f83611613565b925082820190508082111561206757612066611c29565b5b92915050565b7f43616e6469646174652069732064656c657465640000000000000000000000005f82015250565b5f6120a1601483611839565b91506120ac8261206d565b602082019050919050565b5f6020820190508181035f8301526120ce81612095565b9050919050565b7f456e642074696d65206d75737420626520696e207468652066757475726500005f82015250565b5f612109601e83611839565b9150612114826120d5565b602082019050919050565b5f6020820190508181035f830152612136816120fd565b9050919050565b5f6040820190506121505f830185611807565b81810360208301526121628184611857565b90509392505050565b7f456c656374696f6e20616c726561647920656e646564000000000000000000005f82015250565b5f61219f601683611839565b91506121aa8261216b565b602082019050919050565b5f6020820190508181035f8301526121cc81612193565b9050919050565b7f456c656374696f6e2068617320656e64656400000000000000000000000000005f82015250565b5f612207601283611839565b9150612212826121d3565b602082019050919050565b5f6020820190508181035f830152612234816121fb565b9050919050565b7f416c726561647920766f74656420696e207468697320656c656374696f6e00005f82015250565b5f61226f601e83611839565b915061227a8261223b565b602082019050919050565b5f6020820190508181035f83015261229c81612263565b9050919050565b7f596f7520617265206e6f7420656c696769626c6520746f20766f7465000000005f82015250565b5f6122d7601c83611839565b91506122e2826122a3565b602082019050919050565b5f6020820190508181035f830152612304816122cb565b9050919050565b7f43616e64696461746520646f6573206e6f742062656c6f6e6720746f207468695f8201527f7320656c656374696f6e00000000000000000000000000000000000000000000602082015250565b5f612365602a83611839565b91506123708261230b565b604082019050919050565b5f6020820190508181035f83015261239281612359565b9050919050565b7f43616e64696461746520616c72656164792064656c65746564000000000000005f82015250565b5f6123cd601983611839565b91506123d882612399565b602082019050919050565b5f6020820190508181035f8301526123fa816123c1565b9050919050565b5f61240b82611613565b915061241683611613565b925082820390508181111561242e5761242d611c29565b5b92915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603160045260245ffd5b5f6040820190506124745f830185611807565b6124816020830184611807565b9392505050565b7f496e76616c69642063616e6469646174652d656c656374696f6e2070616972005f82015250565b5f6124bc601f83611839565b91506124c782612488565b602082019050919050565b5f6020820190508181035f8301526124e9816124b0565b905091905056fea26469706673582212206f136706af26cf6f2a98c564cbfd740f8d8d78396a402141c69e5a317a8bd08464736f6c634300081e0033";

    public static final String FUNC_ADDCANDIDATE = "addCandidate";

    public static final String FUNC_ALLOWTOVOTE = "allowToVote";

    public static final String FUNC_CANDIDATES = "candidates";

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_DELETECANDIDATE = "deleteCandidate";

    public static final String FUNC_DELETEELECTION = "deleteElection";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ENDELECTION = "endElection";

    public static final String FUNC_GETCANDIDATE = "getCandidate";

    public static final String FUNC_GETTOTALVOTES = "getTotalVotes";

    public static final String FUNC_NEXTCANDIDATEID = "nextCandidateId";

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
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
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.voter = (String) eventValues.getIndexedValues().get(1).getValue();
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
                typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.voter = (String) eventValues.getIndexedValues().get(1).getValue();
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

    public RemoteFunctionCall<TransactionReceipt> allowToVote(BigInteger _electionId, String _voter) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ALLOWTOVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Address(160, _voter)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, BigInteger, String, BigInteger, Boolean>> candidates(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CANDIDATES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, BigInteger, String, BigInteger, Boolean>>(function,
                new Callable<Tuple5<BigInteger, BigInteger, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple5<BigInteger, BigInteger, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, BigInteger, String, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> createElection(String _title, BigInteger _endTime) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_title), 
                new org.web3j.abi.datatypes.generated.Uint256(_endTime)), 
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

    public RemoteFunctionCall<Tuple5<BigInteger, String, Boolean, Boolean, BigInteger>> elections(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ELECTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, String, Boolean, Boolean, BigInteger>>(function,
                new Callable<Tuple5<BigInteger, String, Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, String, Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, Boolean, Boolean, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
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

    public RemoteFunctionCall<Tuple3<String, BigInteger, Boolean>> getCandidate(BigInteger _candidateId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
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

    public RemoteFunctionCall<BigInteger> nextCandidateId() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NEXTCANDIDATEID, 
                Arrays.<Type>asList(), 
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

        public String voter;
    }
}
