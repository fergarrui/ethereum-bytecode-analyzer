package net.nandgr.eth.bytecode.symexecution.evm;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.DecisionsService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import java.util.List;
import java.util.Map;

public class EVMState {

    private final Map<Integer, BytecodeChunk> chunks;
    private boolean isRunning = true;
    // This program counter just points to the chunk offset being executed
    private int pc = 0;
    private final EVMStack stack;
    private final EVMMemory memory;
    private final EVMStorage storage;
    private final EVMEnvironment evmEnvironment;
    private final DecisionsService decisionsService;

    /**
     * This constructor should only be used for cloning
     * (deep copy for reports/filtering)
     * @param state
     */
    public EVMState(EVMState state) {
        chunks = null;
        evmEnvironment = null;
        this.decisionsService = null;
        this.stack = new EVMStack(state.getStack());
        this.memory = new EVMMemory(state.getMemory());
        this.storage = new EVMStorage(state.getStorage());
    }

    public EVMState(Map<Integer, BytecodeChunk> chunks, EVMEnvironment evmEnvironment, DecisionsService decisionsService) {
        this.stack = new EVMStack();
        this.memory = new EVMMemory();
        this.storage = new EVMStorage();
        this.chunks = chunks;
        this.evmEnvironment = evmEnvironment;
        this.decisionsService = decisionsService;
    }

    public Map<Integer, BytecodeChunk> getChunks() {
        return chunks;
    }

    // TODO Logs
    private Object logs;

    public EVMStack getStack() {
        return stack;
    }

    public EVMMemory getMemory() {
        return memory;
    }

    public EVMStorage getStorage() {
        return storage;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public EVMEnvironment getEvmEnvironment() {
        return evmEnvironment;
    }

    public DecisionsService getDecisionsService() {
        return decisionsService;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

    public void addRelation(BytecodeChunk activeChunk, BytecodeChunk branchA, BytecodeChunk branchB) {
        activeChunk.setBranchA(branchA);
        activeChunk.setBranchB(branchB);
    }

    public String printEVMState() {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Inputs:").append(System.lineSeparator());
        if (evmEnvironment != null) {
            sb.append("\tCallData: ").append(evmEnvironment.getCallDataHex()).append(System.lineSeparator());
        }

        sb.append("Program Counter: ").append(pc).append(System.lineSeparator());
        sb.append("# Stack: ").append(stack.printStack()).append(System.lineSeparator());
        sb.append("! Storage: ").append(storage.printStorage()).append(System.lineSeparator());
        sb.append("* Memory: ").append(memory.printMemory()).append(System.lineSeparator());
        return sb.toString();
    }
}
