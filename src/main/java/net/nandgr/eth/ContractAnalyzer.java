package net.nandgr.eth;

import net.nandgr.eth.blockchain.BlockchainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;

public class ContractAnalyzer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ContractAnalyzer.class);

    private final BlockchainService blockchainService;
    private final String address;
    private final BigInteger blockNumber;

    public ContractAnalyzer(BlockchainService blockchainService, String address, BigInteger blockNumber) {
        this.blockchainService = blockchainService;
        this.address = address;
        this.blockNumber = blockNumber;
    }

    // TODO !!!!!
    @Override
    public void run() {
        logger.info("Analyzing contract address={}, block={}", address, blockNumber);
        String contractCode = blockchainService.getContractCode(address, blockNumber);
        logger.debug("Contract code for={}, code={}", address, contractCode);
        Disassembler disassembler = new Disassembler(contractCode);
        logger.info("Disassembled code for={}, disasm={}", address, disassembler.getDisassembledCode());
    }
}
