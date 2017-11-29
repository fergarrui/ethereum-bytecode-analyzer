package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.exceptions.EVMException;

import java.math.BigInteger;

public class Sub extends AbstractOpcode {

    // TODO Consider underflow
    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();
        TraceableWord traceableWord1 = stack.pop();

        TraceTree trace1 = traceableWord0.getTrace();
        TraceTree trace2 = traceableWord1.getTrace();

        BigInteger element0 = new BigInteger(traceableWord0.getBytes());
        BigInteger element1 = new BigInteger(traceableWord1.getBytes());

        BigInteger result = element0.subtract(element1);

        TraceableWord resultTraceableWord = new TraceableWord(result.toByteArray());
        TraceTree traceTree = buildTraceTree(opcode, traceableWord0, traceableWord1, resultTraceableWord);
        traceTree.addChild(trace1);
        traceTree.addChild(trace2);
        resultTraceableWord.setTrace(traceTree);
        stack.push(resultTraceableWord);
    }
}
