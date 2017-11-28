package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.exceptions.EVMException;

import java.math.BigInteger;

public class Add implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();
        TraceableWord traceableWord1 = stack.pop();

        TraceTree trace1 = traceableWord0.getTrace();
        TraceTree trace2 = traceableWord1.getTrace();
        TraceTree traceTree = new TraceTree(opcode);
        traceTree.addChild(trace1);
        traceTree.addChild(trace2);

        BigInteger element0 = new BigInteger(traceableWord0.getBytes());
        BigInteger element1 = new BigInteger(traceableWord1.getBytes());

        BigInteger result = element0.add(element1);

        TraceableWord traceableWord = new TraceableWord(result.toByteArray(), traceTree);
        stack.push(traceableWord);
    }
}
