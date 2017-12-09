package net.nandgr.eth;

import net.nandgr.eth.blockchain.BlockchainExplorer;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment.EVMEnvironmentBuilder;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.diagram.DotDiagram;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

public class BlockchainServiceTest {


    // TODO REMOVE - Test class for development
    @Test
    public void test_disassembler() throws Exception {
//        BlockchainService blockchainService = new BlockchainService();
//        List<String> contractsFromBlock = blockchainService.getContractAddressesFromBlock(3);
//        System.out.println(Arrays.toString(contractsFromBlock.toArray()));
        BlockchainExplorer blockchainExplorer = new BlockchainExplorer();
        blockchainExplorer.exploreFromBlockToBlock(BigInteger.valueOf(1), BigInteger.valueOf(4));
    }

    @Test
    public void test_initial_cfg() throws Exception {

        CFGCreator processor = new CFGCreatorDefault();
        // TODO fix this one
        List<Opcode> opcodes = new Disassembler("6060604052341561000f57600080fd5b60ce8061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633f7a027014604e578063ee919d5014606e575b600080fd5b3415605857600080fd5b606c6004808035906020019091905050608e565b005b3415607857600080fd5b608c60048080359060200190919050506098565b005b8060008190555050565b80600181905550505600a165627a7a7230582048f0df435c3c2549dee0f4b616b4c8a3ccfa900ef7b43c071dd82adec9825c6f0029").getOpcodes();
        ContractBytecode contractBytecode = processor.createContractBytecode(opcodes);

        Map<Integer, BytecodeChunk> chunks = contractBytecode.getFunctionsSection().getChunks();
        DotDiagram.buildDotFile(chunks, "graph", "png");
    }
}
