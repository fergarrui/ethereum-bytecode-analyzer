package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

import java.util.List;

public class SymbolicTransformation {

    private final List<SymbolicTransformation> transformations;
    private final Opcode operation;
    private final int numOfInputs;
    private final TraceableWord constantValue;

    public SymbolicTransformation(List<SymbolicTransformation> transformations, Opcode operation, int numOfInputs, TraceableWord constantValue) {
        this.transformations = transformations;
        this.operation = operation;
        this.numOfInputs = numOfInputs;
        this.constantValue = constantValue;
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

    public TraceableWord getConstantValue() {
        return constantValue;
    }
}
