package net.nandgr.eth.contracts;

import net.nandgr.eth.Disassembler;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.bytecode.symexecution.evm.*;
import net.nandgr.eth.exceptions.EVMException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Ignore;
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
    @Test
    public void test_call_setN() throws Exception {
        final String contractByteCode = "6060604052341561000f57600080fd5b60d88061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633e9552251460535780633f7a0270146079575b6001600081905550005b3415605d57600080fd5b60636099565b6040518082815260200191505060405180910390f35b3415608357600080fd5b6097600480803590602001909190505060a2565b005b60008054905090565b80600081905550505600a165627a7a72305820698e2e766377b7dd448267574b445559f53629f7435ffdcd3a8962ac98201b690029";

        /* setN(99) */
        EVMEnvironment evmEnvironment = new EVMEnvironment.EVMEnvironmentBuilder()
                .setCallData(Hex.decodeHex("3f7a02700000000000000000000000000000000000000000000000000000000000000063"))
                .build();
        EVMState state = runContract(contractByteCode, evmEnvironment);
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();

        Assert.assertEquals(1, stack.size());
        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("3f7a0270"),
                stack.pop().getBytes()));
        TraceableWord expected = new TraceableWord(Hex.decodeHex("0000000000000000000000000000000000000000000000000000000000000063"), null);
        TraceableWord key = new TraceableWord(Hex.decodeHex("00"), null);
        TraceableWord storageContent = storage.get(key);
        Assert.assertTrue(Arrays.equals(expected.getBytes(), storageContent.getBytes()));
    }

    @Test
    public void test_call_getN() throws Exception {
        final String contractByteCode = "6060604052341561000f57600080fd5b60d88061001d6000396000f3006060604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633e9552251460535780633f7a0270146079575b6001600081905550005b3415605d57600080fd5b60636099565b6040518082815260200191505060405180910390f35b3415608357600080fd5b6097600480803590602001909190505060a2565b005b60008054905090565b80600081905550505600a165627a7a72305820698e2e766377b7dd448267574b445559f53629f7435ffdcd3a8962ac98201b690029";

        /* getN() */
        EVMEnvironment evmEnvironment = new EVMEnvironment.EVMEnvironmentBuilder()
                .setCallData(Hex.decodeHex("3e9552250000000000000000000000000000000000000000000000000000000000000063"))
                .build();
        EVMState state = runContract(contractByteCode, evmEnvironment);
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();
        Assert.assertEquals(3, stack.size());

        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("60"),
                stack.pop().getBytes()));

        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("20"),
                stack.pop().getBytes()));

        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("3e955225"),
                stack.pop().getBytes()));

        TraceableWord key = new TraceableWord(Hex.decodeHex("00"), null);
        TraceableWord storageContent = storage.get(key);
        Assert.assertTrue(storageContent == null);
    }

    /*
    pragma solidity ^0.4.18;

    contract Map {

        uint n;
        mapping(uint => string) m1;

        function getN() public view returns (uint) {
            return n;
        }
        function setN(uint num) public {
            n = num;
        }

        function addM(uint num, string s) public {
            m1[num] = s;
        }

        function getM(uint num) public view returns (string) {
            return m1[num];
        }

        function() payable public {
            n = 1;
        }
    }
     */

    @Test
    @Ignore
    public void test_call_addM() throws Exception {
        final String contractByteCode = "6060604052341561000f57600080fd5b6103998061001e6000396000f300606060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806320bec2321461006c5780633e955225146100d25780633f7a0270146100fb578063ff66e6e81461011e575b6001600081905550005b341561007757600080fd5b6100d0600480803590602001909190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506101ba565b005b34156100dd57600080fd5b6100e56101e6565b6040518082815260200191505060405180910390f35b341561010657600080fd5b61011c60048080359060200190919050506101ef565b005b341561012957600080fd5b61013f60048080359060200190919050506101f9565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561017f578082015181840152602081019050610164565b50505050905090810190601f1680156101ac5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806001600084815260200190815260200160002090805190602001906101e19291906102b4565b505050565b60008054905090565b8060008190555050565b610201610334565b600160008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102a85780601f1061027d576101008083540402835291602001916102a8565b820191906000526020600020905b81548152906001019060200180831161028b57829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102f557805160ff1916838001178555610323565b82800160010185558215610323579182015b82811115610322578251825591602001919060010190610307565b5b5090506103309190610348565b5090565b602060405190810160405280600081525090565b61036a91905b8082111561036657600081600090555060010161034e565b5090565b905600a165627a7a723058202858187d1afd161f9d912e861c223b65a45a64145648853e2c4e398b6e0107ad0029";
        /* addM(123,"AA") */
        EVMEnvironment evmEnvironment = new EVMEnvironment.EVMEnvironmentBuilder()
                .setCallData(Hex.decodeHex("20bec232000000000000000000000000000000000000000000000000000000000000007b000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000024141000000000000000000000000000000000000000000000000000000000000"))
                .build();
        EVMState state = runContract(contractByteCode, evmEnvironment);

        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();
        Assert.assertEquals(1, stack.size());
        Assert.assertTrue(Arrays.equals(
                Hex.decodeHex("20bec232"),
                stack.pop().getBytes()));

        TraceableWord expected = new TraceableWord(Hex.decodeHex("4141000000000000000000000000000000000000000000000000000000000004"), null);
        TraceableWord key = new TraceableWord(Hex.decodeHex("7754e603b51d4c4a3aba7ace3043385dc37d93dbb4164d6e123ba69433d7f0a7"), null);
        TraceableWord storageContent = storage.get(key);
        Assert.assertTrue(Arrays.equals(expected.getBytes(), storageContent.getBytes()));
    }

    private static EVMState runContract(String contractCode, EVMEnvironment evmEnvironment) throws EVMException {
        Disassembler disassembler = new Disassembler(contractCode);
        CFGCreator cfgCreator = new CFGCreatorDefault();
        ContractBytecode contractBytecode = cfgCreator.createContractBytecode(disassembler.getOpcodes());
        Map<Integer, BytecodeChunk> functionsChunks = contractBytecode.getFunctionsSection().getChunks();
        cfgCreator.createRelations(functionsChunks);
        SymExecutor symExecutor = new SymExecutor(functionsChunks, evmEnvironment);
        symExecutor.execute();
        return symExecutor.getState();
    }
}
