package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;

import java.math.BigInteger;
import java.util.Collections;

public class CallValue extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        BigInteger callValue = state.getEvmEnvironment().getCallValue();
        EVMStack stack = state.getStack();

        TraceableWord traceableWord = new TraceableWord(callValue.toByteArray());
        TraceTree traceTree = buildTraceTree(opcode, traceableWord, Collections.emptyList());
        traceableWord.setTrace(traceTree);
        stack.push(traceableWord);
    }
}
