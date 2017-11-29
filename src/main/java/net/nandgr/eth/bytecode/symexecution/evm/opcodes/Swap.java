package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

public class Swap extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        int swapN = opcode.getOpcode().getOpcode() - Opcodes.SWAP1.getOpcode()  + 1;
        swap(swapN, state.getStack(), opcode);
    }

    private void swap(int n, EVMStack stack, Opcode opcode) {
        if (n >= stack.size() || n < 1) {
            throw new IllegalArgumentException("Input must be 1 < input < stack.size()");
        }
        TraceableWord elem = stack.pop();
        int index = stack.size() - n;
        TraceableWord elemToSwap = stack.get(index);
        stack.insertElementAt(elem, index);
        stack.remove(index+1);
        TraceTree traceTree = buildTraceTree(opcode, elem, elemToSwap);
        elem.getTrace().addChild(traceTree);
        elemToSwap.getTrace().addChild(traceTree);
        stack.push(elemToSwap);
    }
}
