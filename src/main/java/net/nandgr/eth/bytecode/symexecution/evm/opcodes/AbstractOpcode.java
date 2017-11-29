package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.ExecutionTrace;
import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractOpcode implements OpcodeExecutor {

    public abstract void execute(EVMState state, Opcode opcode) throws EVMException;

    protected static TraceTree buildTraceTree(Opcode opcode, TraceableWord result, List<TraceableWord> inputs) {
        List<SymbolicTransformation> symbolicTransformationsList = new ArrayList<>();
        int numInputs = 0;
        boolean isSymbolic = OpcodeUtils.isSymbolic(opcode.getOpcode());
        for (TraceableWord word : inputs) {
            ExecutionTrace executionTrace = word.getTrace().getExecutionTrace();
            SymbolicTransformation symbolicTransformation = executionTrace.getSymbolicTransformation();
            symbolicTransformationsList.add(symbolicTransformation);
            numInputs += symbolicTransformation.getNumOfInputs();
            isSymbolic |= executionTrace.isSymbolic();
        }
        SymbolicTransformation symbolicTransformation = new SymbolicTransformation(symbolicTransformationsList, opcode, numInputs);
        List<TraceableWord> outputs = result == null ? Collections.emptyList() : Lists.of(result);
        ExecutionTrace executionTrace = new ExecutionTrace(opcode, inputs, outputs, symbolicTransformation, isSymbolic);
        return new TraceTree(executionTrace);
    }
}
