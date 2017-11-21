package net.nandgr.eth.bytecode.symexecution.evm;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class EVMState {

    private boolean isRunning = true;
    // This program counter just points to the chunk offset being executed
    private int pc = 0;
    private final EVMStack stack = new EVMStack();
    private final EVMMemory memory = new EVMMemory();
    private final EVMStorage storage = new EVMStorage();
    private final EVMEnvironment evmEnvironment;

    public EVMState(EVMEnvironment evmEnvironment) {
        this.evmEnvironment = evmEnvironment;
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

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

    public String printEVMState() {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Inputs:").append(System.lineSeparator());
        List<Byte> callData = evmEnvironment.getCallData();
        byte[] bytes = ArrayUtils.toPrimitive(callData.toArray(new Byte[callData.size()]));
        sb.append("\tCallData: ").append(Hex.encodeHexString(bytes)).append(System.lineSeparator());

        sb.append("Program Counter: ").append(pc).append(System.lineSeparator());
        sb.append("# Stack: ").append(stack.printStack()).append(System.lineSeparator());
        sb.append("! Storage: ").append(storage.printStorage()).append(System.lineSeparator());
        sb.append("* Memory: ").append(memory.printMemory()).append(System.lineSeparator());
        return sb.toString();
    }
}
