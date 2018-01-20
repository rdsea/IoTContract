package at.ac.tuwien.dsg.thinggovernor.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class Logstore extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6105df8061001e6000396000f300606060405263ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166386a3cd278114610068578063978acaf3146100f2578063b7988fea1461015e578063cde216471461018a578063cedf6616146101dd57600080fd5b341561007357600080fd5b61007b610270565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100b757808201518382015260200161009f565b50505050905090810190601f1680156100e45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156100fd57600080fd5b61007b6004803573ffffffffffffffffffffffffffffffffffffffff169060446024803590810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506102a795505050505050565b341561016957600080fd5b61007b73ffffffffffffffffffffffffffffffffffffffff600435166103da565b341561019557600080fd5b6101db60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061042595505050505050565b005b34156101e857600080fd5b6101db60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284375094965061046595505050505050565b60408051908101604052600781527f64656661756c7400000000000000000000000000000000000000000000000000602082015281565b6102af610506565b73ffffffffffffffffffffffffffffffffffffffff831660009081526020819052604090819020908390518082805190602001908083835b602083106103065780518252601f1990920191602091820191016102e7565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905092915050565b6103e2610506565b61041f8260408051908101604052600781527f64656661756c740000000000000000000000000000000000000000000000000060208201526102a7565b92915050565b61046260408051908101604052600781527f64656661756c7400000000000000000000000000000000000000000000000000602082015282610465565b50565b73ffffffffffffffffffffffffffffffffffffffff33166000908152602081905260409081902082918490518082805190602001908083835b602083106104bd5780518252601f19909201916020918201910161049e565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051610501929160200190610518565b505050565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061055957805160ff1916838001178555610586565b82800160010185558215610586579182015b8281111561058657825182559160200191906001019061056b565b50610592929150610596565b5090565b6105b091905b80821115610592576000815560010161059c565b905600a165627a7a7230582088bc2215416f7fe6263889ce98cd9b6ca73f2fc0dc39d02a57466ba6b622d0d90029";

    private Logstore(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Logstore(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<Utf8String> defaultKey() {
        Function function = new Function("defaultKey", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Utf8String> getLog(Address owner, Utf8String key) {
        Function function = new Function("getLog", 
                Arrays.<Type>asList(owner, key), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Utf8String> getDefaultLog(Address owner) {
        Function function = new Function("getDefaultLog", 
                Arrays.<Type>asList(owner), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> setDefaultLog(Utf8String value) {
        Function function = new Function("setDefaultLog", Arrays.<Type>asList(value), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> setLog(Utf8String key, Utf8String value) {
        Function function = new Function("setLog", Arrays.<Type>asList(key, value), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<Logstore> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Logstore.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<Logstore> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Logstore.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Logstore load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Logstore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Logstore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Logstore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
