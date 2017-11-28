package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import java.util.List;

public class SymbolicTransformation {

    private final List<SymbolicTransformation> inputs;
    private final Opcode operation;

    public SymbolicTransformation(List<SymbolicTransformation> inputs, Opcode operation) {
        this.inputs = inputs;
        this.operation = operation;
    }
}
