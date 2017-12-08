package net.nandgr.eth;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.DecisionsService;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment.EVMEnvironmentBuilder;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.exceptions.EVMException;

import java.util.Arrays;
import java.util.Map;

public class AbstractSymbolicTest {

    public static SymbolicPathsHandler symPathFinder(Map<Integer, BytecodeChunk> chunks) {
        return new SymbolicPathsHandler(chunks);
    }

    public static EVMState symExecute(Map<Integer, BytecodeChunk> chunks) throws EVMException {
        EVMEnvironment inputs = new EVMEnvironmentBuilder().build();
        return symExecute(chunks, inputs);
    }

    public static EVMState symExecute(Map<Integer, BytecodeChunk> chunks, EVMEnvironment inputs) throws EVMException {
        SymExecutor symExecutor = new SymExecutor(chunks, inputs, new DecisionsService());
        return symExecutor.execute();
    }

    public static BytecodeChunk createChunk(int id, Opcode... op) {
        BytecodeChunk chunk = new BytecodeChunk(id);
        chunk.setOpcodes(Arrays.asList(op));
        return chunk;
    }

    public static EVMEnvironment createDefaultEnvironment() {
        return new EVMEnvironmentBuilder().build();
    }
}
