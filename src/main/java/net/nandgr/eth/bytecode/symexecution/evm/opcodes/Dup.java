package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.ExecutionTrace;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.utils.Lists;

import java.util.Collections;
import java.util.Stack;

public class Dup extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        int dupN = opcode.getOpcode().getOpcode() - Opcodes.DUP1.getOpcode() + 1;
        EVMStack stack = state.getStack();
        if (dupN > stack.size() || dupN < 0) {
            throw new IllegalArgumentException("Input must be 0 < input <= stack.size() + (" + stack.size() + "), but is " + dupN);
        }
        int index = stack.size() - dupN;
        TraceableWord elemToDup = stack.get(index);

        TraceTree traceTree = buildTraceTree(opcode, elemToDup, elemToDup);
        elemToDup.getTrace().addChild(traceTree);
        stack.push(elemToDup);
    }
}
