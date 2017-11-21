package net.nandgr.eth.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment.EVMEnvironmentBuilder;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.exceptions.EVMException;
import java.util.Arrays;
import java.util.Map;

class AbstractOpcodesTest {

    static EVMState symExecute(Map<Integer, BytecodeChunk> chunks) throws EVMException {
        EVMEnvironment inputs = new EVMEnvironmentBuilder().build();
        return symExecute(chunks, inputs);
    }

    static EVMState symExecute(Map<Integer, BytecodeChunk> chunks, EVMEnvironment inputs) throws EVMException {
        SymExecutor symExecutor = new SymExecutor(chunks, inputs);
        symExecutor.execute();
        return symExecutor.getState();
    }

    static BytecodeChunk createChunk(int id, Opcode... op) {
        BytecodeChunk chunk = new BytecodeChunk(id);
        chunk.setOpcodes(Arrays.asList(op));
        return chunk;
    }
}
