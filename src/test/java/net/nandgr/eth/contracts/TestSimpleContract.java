package net.nandgr.eth.contracts;

import net.nandgr.eth.Disassembler;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Map;

public class TestSimpleContract {

/*
* pragma solidity ^0.4.18;
* contract T2 {
*     uint n;
*     function getN() public view returns (uint) {
*         return n;
*     }
*     function setN(uint num) public {
*         n = num;
*     }
*     function() payable public {
*         n = 1;
*     }
* }
*/

    private static final String contractByteCode = "6060604052341561000f57600080fd5b60d88061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633e9552251460535780633f7a0270146079575b6001600081905550005b3415605d57600080fd5b60636099565b6040518082815260200191505060405180910390f35b3415608357600080fd5b6097600480803590602001909190505060a2565b005b60008054905090565b80600081905550505600a165627a7a72305820698e2e766377b7dd448267574b445559f53629f7435ffdcd3a8962ac98201b690029";

    @Test
    public void test_call_setN() throws Exception {
        Disassembler disassembler = new Disassembler(contractByteCode);
        CFGCreator cfgCreator = new CFGCreatorDefault();
        ContractBytecode contractBytecode = cfgCreator.createContractBytecode(disassembler.getOpcodes());
        Map<Integer, BytecodeChunk> functionsChunks = contractBytecode.getFunctionsSection().getChunks();
        cfgCreator.createRelations(functionsChunks);

        /* setN(99) */
        EVMEnvironment evmEnvironment = new EVMEnvironment.EVMEnvironmentBuilder()
                .setCallData(Hex.decodeHex("3f7a02700000000000000000000000000000000000000000000000000000000000000063"))
                .build();
        SymExecutor symExecutor = new SymExecutor(functionsChunks, evmEnvironment);
        symExecutor.execute();
        EVMState state = symExecutor.getState();
        EVMStack stack = state.getStack();
        Assert.assertEquals(1, stack.size());
        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("3f7a0270"),
                stack.pop().getBytes()));

    }
}
