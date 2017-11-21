package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.evm.TraceTree;
import net.nandgr.eth.exceptions.EVMException;

import java.math.BigInteger;
import java.util.Stack;

public class IsZero implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord traceableWord0 = stack.pop();

        TraceTree<Opcode> trace1 = traceableWord0.getTrace();
        TraceTree<Opcode> traceTree = new TraceTree<>(opcode);
        traceTree.addChild(trace1);

        BigInteger element0 = new BigInteger(traceableWord0.getBytes());

        byte[] bytes = {0x00};
        if (element0.equals(BigInteger.valueOf(0))) {
            bytes[0] = 0x01;
        }
        TraceableWord traceableWord = new TraceableWord(bytes, traceTree);
        stack.push(traceableWord);
    }
}
