package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import java.util.List;

public class ExecutionTrace {
    private final Opcode opcode;
    private final List<TraceableWord> input;
    private final List<TraceableWord> output;
    private final SymbolicTransformation symbolicTransformation;

    public ExecutionTrace(Opcode opcode, List<TraceableWord> input, List<TraceableWord> output, SymbolicTransformation symbolicTransformation) {
        this.opcode = opcode;
        this.input = input;
        this.output = output;
        this.symbolicTransformation = symbolicTransformation;
    }
}
