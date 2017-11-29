package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.math.BigInteger;

public class Add extends AbstractOpcode {

    // TODO consider overflow
    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();
        TraceableWord traceableWord1 = stack.pop();

        BigInteger element0 = new BigInteger(traceableWord0.getBytes());
        BigInteger element1 = new BigInteger(traceableWord1.getBytes());
        BigInteger result = element0.add(element1);
        TraceableWord resultWord = new TraceableWord(result.toByteArray());

        TraceTree traceTree = buildTraceTree(opcode, resultWord, Lists.of(traceableWord0, traceableWord1));

        traceTree.addChild(traceableWord0.getTrace());
        traceTree.addChild(traceableWord1.getTrace());
        resultWord.setTrace(traceTree);
        stack.push(resultWord);
    }
}
