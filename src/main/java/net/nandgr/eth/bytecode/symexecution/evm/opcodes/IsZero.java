package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.math.BigInteger;

public class IsZero extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();

        TraceTree trace1 = traceableWord0.getTrace();

        BigInteger element0 = new BigInteger(traceableWord0.getBytes());

        byte[] bytes = {0x00};
        if (element0.equals(BigInteger.valueOf(0))) {
            bytes[0] = 0x01;
        }
        TraceableWord resultTraceableWord = new TraceableWord(bytes);
        TraceTree traceTree = buildTraceTree(opcode, resultTraceableWord, Lists.of(traceableWord0));
        traceTree.addChild(trace1);
        resultTraceableWord.setTrace(traceTree);
        stack.push(resultTraceableWord);
    }
}
