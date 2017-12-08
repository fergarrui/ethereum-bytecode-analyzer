package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.Opcodes;
import java.math.BigInteger;

public class ProcessChildResult {
    private final BigInteger result;
    private final Opcodes opcode;

    public ProcessChildResult(BigInteger result, Opcodes opcode) {
        this.result = result;
        this.opcode = opcode;
    }

    public BigInteger getResult() {
        return result;
    }

    public Opcodes getOpcode() {
        return opcode;
    }
}
