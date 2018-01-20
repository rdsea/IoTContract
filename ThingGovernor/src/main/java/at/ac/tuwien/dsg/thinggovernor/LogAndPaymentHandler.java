/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thinggovernor;

import at.ac.tuwien.dsg.thinggovernor.contract.Logstore;
import at.ac.tuwien.dsg.thinggovernor.model.LogEntry;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

/**
 *
 * @author Peter Klein
 */
@Component
public class LogAndPaymentHandler {
    
    static final Logger log = Logger.getLogger(LogAndPaymentHandler.class.getName());
    
    private Map<String,Set<LogEntry>> logStore = new HashMap<>();
    
    private String address;
    
    private boolean useBlockchain = false;
    
    @Autowired
    ConfigHandler configHandler;
    
    Web3j web3 = null;
    //private final static String PRIVATEKEY = "4f3edf983ac636a65a842ce7c78d9aa706d3b113bce9c46f30d7d21715b23b1d";
    
    // see https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_PRICE_LOCAL = BigInteger.valueOf(20_000_000_000L);
	
	// http://ethereum.stackexchange.com/questions/1832/cant-send-transaction-exceeds-block-gas-limit-or-intrinsic-gas-too-low
    public static final BigInteger GAS_LIMIT_LOCAL = BigInteger.valueOf(500_000L);
    
    
    private Map<String, Logstore> scLogs = new HashMap<>();
    
    private void showClientVersion() {
        web3.web3ClientVersion().observable().subscribe(x -> {
            String clientVersion = x.getWeb3ClientVersion();
            System.out.println(clientVersion);
        });
    }
    
    public boolean deployLogContract(String contract) {
        try {
            if (web3 == null) {
                if (configHandler.getConfigData().getUseBlockchain() != null && configHandler.getConfigData().getUseBlockchain().equals("yes")) {
                    useBlockchain = true;
                    web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
                    showClientVersion(); 
                }
            }
            Credentials credentials = Credentials.create(configHandler.getConfigData().getPrivateKey());
            address = credentials.getAddress();
            log.info("address=" + address);
            if (useBlockchain) {
                Logstore sc = Logstore.deploy(web3, credentials, GAS_PRICE_LOCAL, GAS_LIMIT_LOCAL, BigInteger.ZERO).get();
                log.info(sc.getContractAddress());
                scLogs.put(contract, sc);
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
        
    public void addLog(LogEntry logEntry) {
        try {
            if (logStore.get(logEntry.getServiceTemplate()) == null) {
                Set logEntries = new HashSet();
                logEntries.add(logEntry);
                logStore.put(logEntry.getServiceTemplate(), logEntries);
            } else {
                Set logEntries = logStore.get(logEntry.getServiceTemplate());
                logEntries.add(logEntry);
            }
            if (useBlockchain) {
                TransactionReceipt tr = scLogs.get(logEntry.getServiceTemplate())
                    .setLog(new Utf8String(logEntry.getId()), 
                            new Utf8String (logEntry.getDigest())).get();
                log.info("logged to block" + tr.getBlockNumber());
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return;
        }
        
    }
    
    public Set readLogs(String serviceTemplate) {
        return logStore.get(serviceTemplate);
    }
    
    public String readScLogs(String serviceTemplate, String id) {
        try {
            Logstore sc = scLogs.get(serviceTemplate);
            Utf8String digest = sc.getLog(new Address(address), new Utf8String(id)).get();
            log.info("digest retrieved=" + digest.toString());
            return digest.toString();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    void makePayment(String fromAddress, String toAddress, BigInteger amountWei)
			throws Exception
    {
        System.out.println("Accounts[1] (to address) " + toAddress + "\n" + 
                        "Balance before Tx: " + getBalanceEther(web3, toAddress) + "\n");

        System.out.println("Transfer " + weiToEther(amountWei) + " Ether to account");

        // step 1: get the nonce (tx count for sending address)
        EthGetTransactionCount transactionCount = web3
                        .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();

        BigInteger nonce = transactionCount.getTransactionCount();
        System.out.println("Nonce for sending address (coinbase): " + nonce);

        // step 2: create the transaction object
        Transaction transaction = Transaction
                        .createEtherTransaction(
                                        fromAddress, 
                                        nonce, 
                                        GAS_PRICE_LOCAL, 
                                        GAS_LIMIT_LOCAL, 
                                        toAddress, 
                                        amountWei);

        // step 3: send the tx to the network
        EthSendTransaction response = web3
                        .ethSendTransaction(transaction)
                        .sendAsync()
                        .get();

        String txHash = response.getTransactionHash();		
        System.out.println("Tx hash: " + txHash);

        // step 4: wait for the confirmation of the network
        TransactionReceipt receipt = waitForReceipt(web3, txHash);

        BigInteger gasUsed = receipt.getCumulativeGasUsed();
        System.out.println("Tx cost: " + gasUsed + " Gas (" + 
                        weiToEther(gasUsed.multiply(GAS_PRICE_LOCAL)) +" Ether)\n");

        System.out.println("Balance after Tx: " + getBalanceEther(web3, toAddress));
    }
    
    private static BigDecimal getBalanceEther(Web3j web3j, String address) throws InterruptedException, ExecutionException {
        return weiToEther(getBalanceWei(web3j, address));
    }
    
    private static BigInteger getBalanceWei(Web3j web3j, String address) throws InterruptedException, ExecutionException {
        EthGetBalance balance = web3j
                        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();

        return balance.getBalance();
    }
    
    private static BigDecimal weiToEther(BigInteger wei) {
            return Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
    }
    
    private static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) 
			throws Exception 
    {

        int attempts = 40;
        int sleep_millis = 1000;

        Optional<TransactionReceipt> receipt = getReceipt(web3j, transactionHash);

        while(attempts-- > 0 && !receipt.isPresent()) {
                Thread.sleep(sleep_millis);
                receipt = getReceipt(web3j, transactionHash);
        }

        if (attempts <= 0) {
                throw new RuntimeException("No Tx receipt received");
        }

        return receipt.get();
    }
    
    private static Optional<TransactionReceipt> getReceipt(Web3j web3j, String transactionHash) 
			throws Exception 
    {
        EthGetTransactionReceipt receipt = web3j
                        .ethGetTransactionReceipt(transactionHash)
                        .sendAsync()
                        .get();

        return receipt.getTransactionReceipt();
    }
    
}
