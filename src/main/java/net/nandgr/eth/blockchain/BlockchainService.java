package net.nandgr.eth.blockchain;

import net.nandgr.eth.exceptions.BlockNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);
    private Web3j web3;
    private static final String EMPTY_ADDRESS = "0x0";

    public BlockchainService() {
        this.web3 = Web3j.build(new HttpService("http://localhost:9545"));
    }

    public List<TransactionObject> getTransactionsFromBlock(BigInteger blockNumber) throws BlockNotFoundException {
        try {
            EthBlock ethBlock = web3.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true).send();
            if (ethBlock == null || ethBlock.getBlock() == null) {
                throw new BlockNotFoundException("Block number: " + blockNumber + " was not found");
            }
            return ethBlock.getBlock().getTransactions().stream()
                    .map(TransactionObject.class::cast)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error getTransactionsFromBlock", e);
        }
        return Collections.emptyList();
    }

    public TransactionReceipt getTransactionReceipt(String hash) {
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = web3.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
        } catch (IOException e) {
            logger.error("Error getTransactionReceipt", e);
        }
        return transactionReceipt;
    }

    public List<String> getContractAddressesFromBlock(BigInteger blockNumber) throws BlockNotFoundException {
        return getTransactionsFromBlock(blockNumber).stream()
                .filter(tx -> tx.getTo().equals(EMPTY_ADDRESS))
                .map(tx-> getTransactionReceipt(tx.getHash()).getContractAddress())
                .collect(Collectors.toList());
    }

    public String getContractCode(String contractAddress, BigInteger blockNumber) {
        try {
            return web3.ethGetCode(contractAddress, DefaultBlockParameter.valueOf(blockNumber)).send().getCode();
        } catch (IOException e) {
            logger.error("Error getContractCode", e);
        }
        return "";
    }
}
