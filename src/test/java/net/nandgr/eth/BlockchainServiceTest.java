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


    // TODO REMOVE
    @Test
    public void test_disassembler() throws Exception {
//        BlockchainService blockchainService = new BlockchainService();
//        List<String> contractsFromBlock = blockchainService.getContractAddressesFromBlock(3);
//        System.out.println(Arrays.toString(contractsFromBlock.toArray()));
        BlockchainExplorer blockchainExplorer = new BlockchainExplorer();
        blockchainExplorer.exploreFromBlockToBlock(BigInteger.valueOf(1), BigInteger.valueOf(4));
    }

    @Test
    public void test_processor() throws Exception {

        CFGCreator processor = new CFGCreatorDefault();
        List<Opcode> opcodes = new Disassembler("6060604052341561000f57600080fd5b60d88061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633e9552251460535780633f7a0270146079575b6001600081905550005b3415605d57600080fd5b60636099565b6040518082815260200191505060405180910390f35b3415608357600080fd5b6097600480803590602001909190505060a2565b005b60008054905090565b80600081905550505600a165627a7a72305820698e2e766377b7dd448267574b445559f53629f7435ffdcd3a8962ac98201b690029").getOpcodes();

        ContractBytecode contractBytecode = processor.createContractBytecode(opcodes);
        String s = DotDiagram.buildDotFormat(contractBytecode.getFunctionsSection().getChunks());
        System.out.println(s);

    }

    @Test
    public void name2() throws Exception {
        Map<Integer, BytecodeChunk> bytecodeChunks = new HashMap<>();
        List<Opcode> opcodesChunk1 = new ArrayList<>();
        List<Opcode> opcodesChunk2 = new ArrayList<>();
        Opcode o1 = new Opcode();o1.setOpcode(Opcodes.PUSH1);o1.setParameter(BigInteger.valueOf(64));
        Opcode o2 = new Opcode();o2.setOpcode(Opcodes.PUSH4);o2.setParameter(BigInteger.valueOf(1064960624));
        Opcode o3 = new Opcode();o3.setOpcode(Opcodes.PUSH1);o3.setParameter(BigInteger.valueOf(99));
        Opcode o4 = new Opcode();o4.setOpcode(Opcodes.SWAP1);
        Opcode o5 = new Opcode();o5.setOpcode(Opcodes.SWAP2);
        Opcode o6 = new Opcode();o6.setOpcode(Opcodes.JUMP);

        opcodesChunk1.add(o1);
        opcodesChunk1.add(o2);
        opcodesChunk1.add(o3);
        opcodesChunk1.add(o4);
        opcodesChunk1.add(o5);
        opcodesChunk1.add(o6);

        Opcode o7 = new Opcode();o7.setOpcode(Opcodes.PUSH1);o7.setParameter(BigInteger.valueOf(0));
        Opcode o8 = new Opcode();o8.setOpcode(Opcodes.STOP);

        opcodesChunk2.add(o7);
        opcodesChunk2.add(o8);

        BytecodeChunk bytecodeChunk1 = new BytecodeChunk(1);
        bytecodeChunk1.setOpcodes(opcodesChunk1);

        BytecodeChunk bytecodeChunk2 = new BytecodeChunk(2);
        bytecodeChunk2.setOpcodes(opcodesChunk2);

        bytecodeChunks.put(0, bytecodeChunk1);
        bytecodeChunks.put(64, bytecodeChunk2);
        EVMEnvironment inputs = new EVMEnvironmentBuilder().build();
        SymExecutor symExecutor = new SymExecutor(bytecodeChunks, inputs);
        symExecutor.execute();
    }

    @Test
    public void name() throws Exception {
        Stack<Integer> stack = new Stack<>();
        stack.push(4);
        stack.push(3);
        stack.push(2);
        stack.push(1);
        System.out.println("BEFORE : " + stack);
        System.out.println("AFTER : " + stack);
    }
}
