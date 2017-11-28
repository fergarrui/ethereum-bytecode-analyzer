package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.ExecutionTrace;
import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.math.BigInteger;
import java.util.Collections;

public class Push implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        BigInteger parameter = opcode.getParameter();
        TraceableWord traceableWord = new TraceableWord(parameter.toByteArray());

        SymbolicTransformation symbolicTransformation = new SymbolicTransformation(Collections.emptyList(), opcode);
        ExecutionTrace executionTrace = new ExecutionTrace(opcode, Collections.emptyList(), Lists.of(traceableWord), symbolicTransformation);
        TraceTree traceTree = new TraceTree(executionTrace);
        traceableWord.setTrace(traceTree);

        state.getStack().push(traceableWord);
    }
}
