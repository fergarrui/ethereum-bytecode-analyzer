package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

import java.util.Stack;

public class JumpI implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        EVMStack stack = state.getStack();
        TraceableWord jumpTo = stack.pop();
        TraceableWord jumpIf = stack.pop();
        jumpTo.getTrace().addChild(opcode);
        jumpIf.getTrace().addChild(opcode);
        if (jumpIf.getIntData() != 0) {
            state.setPc(jumpTo.getIntData());
        } else {
            state.setPc(opcode.getOffset() + 1);
        }
    }
}
