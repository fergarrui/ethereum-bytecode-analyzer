package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.exceptions.EVMException;

import java.math.BigInteger;

public class LT extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();
        TraceableWord traceableWord1 = stack.pop();


        BigInteger element0 = new BigInteger(traceableWord0.getBytes());
        BigInteger element1 = new BigInteger(traceableWord1.getBytes());

        byte[] bytes = {0x00};
        if (element0.compareTo(element1) < 0) {
            bytes[0] = 0x01;
        }
        TraceableWord resultTraceableWord = new TraceableWord(bytes);
        TraceTree traceTree = buildTraceTree(opcode, traceableWord0, traceableWord1, resultTraceableWord);
        traceTree.addChild(traceableWord0.getTrace());
        traceTree.addChild(traceableWord1.getTrace());
        resultTraceableWord.setTrace(traceTree);
        stack.push(resultTraceableWord);
    }
}
