package net.nandgr.eth.blockchain;

import net.nandgr.eth.ContractAnalyzer;
import net.nandgr.eth.Parameters;
import net.nandgr.eth.exceptions.BlockNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockchainExplorer {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainExplorer.class);

    private final BlockchainService blockchainService = new BlockchainService();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Parameters.MAX_THREADS);
    private final Map<String, Contract> contracts = new HashMap<>();

    public void exploreFromBlockToBlock(BigInteger blockStart, BigInteger blockEnd) {
        if (blockStart.compareTo(BigInteger.valueOf(1)) < 0 || blockStart.compareTo(blockEnd) >= 0) {
            throw new IllegalArgumentException("Block start cannot be lower than 1 or higher than block end");
        }
        for (BigInteger i = blockStart; i.compareTo(blockEnd) <= 0 ; i = i.add(BigInteger.valueOf(1))) {
            final BigInteger blockNum = i;
            List<String> contractAddressesFromBlock = null;
            try {
                contractAddressesFromBlock = blockchainService.getContractAddressesFromBlock(i);
                contractAddressesFromBlock.stream().forEach(address -> {
                    executorService.execute(new ContractAnalyzer(blockchainService, address, blockNum));
                });
            } catch (BlockNotFoundException e) {
                logger.error("BlockNotFound", e);
            }

        }
    }
}
