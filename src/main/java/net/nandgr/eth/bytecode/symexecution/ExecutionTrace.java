package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import java.util.List;

public class ExecutionTrace {
    private final Opcode opcode;
    private final List<TraceableWord> input;
    private final List<TraceableWord> output;
    private final SymbolicTransformation symbolicTransformation;
    private final boolean isSymbolic;

    public ExecutionTrace(Opcode opcode, List<TraceableWord> input, List<TraceableWord> output, SymbolicTransformation symbolicTransformation, boolean isSymbolic) {
        this.opcode = opcode;
        this.input = input;
        this.output = output;
        this.symbolicTransformation = symbolicTransformation;
        this.isSymbolic = isSymbolic;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public List<TraceableWord> getInput() {
        return input;
    }

    public List<TraceableWord> getOutput() {
        return output;
    }

    public SymbolicTransformation getSymbolicTransformation() {
        return symbolicTransformation;
    }

    public boolean isSymbolic() {
        return isSymbolic;
    }
}
