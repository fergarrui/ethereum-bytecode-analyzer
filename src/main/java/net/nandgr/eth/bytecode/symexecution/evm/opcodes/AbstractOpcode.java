package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.ExecutionTrace;
import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;
import java.util.List;

public abstract class AbstractOpcode implements OpcodeExecutor {

    public abstract void execute(EVMState state, Opcode opcode) throws EVMException;

    // TODO merge both methods and allow an array of inputs as parameter
    protected TraceTree buildTraceTree(Opcode opcode, TraceableWord traceableWord0, TraceableWord traceableWord1, TraceableWord resultWord) {
        TraceTree trace0 = traceableWord0.getTrace();
        TraceTree trace1 = traceableWord1.getTrace();

        ExecutionTrace executionTrace0 = trace0.getExecutionTrace();
        ExecutionTrace executionTrace1 = trace1.getExecutionTrace();

        SymbolicTransformation trace0SymbolicTransformation = executionTrace0.getSymbolicTransformation();
        SymbolicTransformation trace1SymbolicTransformation = executionTrace1.getSymbolicTransformation();
        List<SymbolicTransformation> symbolicTransformationsList = Lists.of(
                trace0SymbolicTransformation,
                trace1SymbolicTransformation);
        int numOfInputs = trace0SymbolicTransformation.getNumOfInputs() + trace1SymbolicTransformation.getNumOfInputs();

        SymbolicTransformation symbolicTransformation = new SymbolicTransformation( symbolicTransformationsList, opcode, numOfInputs);
        boolean isSymbolic = executionTrace0.isSymbolic() || executionTrace1.isSymbolic();
        ExecutionTrace executionTrace = new ExecutionTrace(opcode, Lists.of(traceableWord0, traceableWord1), Lists.of(resultWord), symbolicTransformation, isSymbolic);
        return new TraceTree(executionTrace);
    }

    protected TraceTree buildTraceTree(Opcode opcode, TraceableWord traceableWord0, TraceableWord resultWord) {
        TraceTree trace0 = traceableWord0.getTrace();

        ExecutionTrace executionTrace0 = trace0.getExecutionTrace();

        SymbolicTransformation trace0SymbolicTransformation = executionTrace0.getSymbolicTransformation();
        List<SymbolicTransformation> symbolicTransformationsList = Lists.of(
                trace0SymbolicTransformation);
        int numOfInputs = trace0SymbolicTransformation.getNumOfInputs();

        SymbolicTransformation symbolicTransformation = new SymbolicTransformation( symbolicTransformationsList, opcode, numOfInputs);
        boolean isSymbolic = executionTrace0.isSymbolic();
        ExecutionTrace executionTrace = new ExecutionTrace(opcode, Lists.of(traceableWord0), Lists.of(resultWord), symbolicTransformation, isSymbolic);
        return new TraceTree(executionTrace);
    }
}
