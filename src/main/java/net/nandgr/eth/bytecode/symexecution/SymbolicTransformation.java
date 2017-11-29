package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import java.util.List;

public class SymbolicTransformation {

    private final List<SymbolicTransformation> transformations;
    private final Opcode operation;
    private final int numOfInputs;

    public SymbolicTransformation(List<SymbolicTransformation> transformations, Opcode operation, int numOfInputs) {
        this.transformations = transformations;
        this.operation = operation;
        this.numOfInputs = numOfInputs;
    }

    public List<SymbolicTransformation> getTransformations() {
        return transformations;
    }

    public Opcode getOperation() {
        return operation;
    }

    public int getNumOfInputs() {
        return numOfInputs;
    }
}
