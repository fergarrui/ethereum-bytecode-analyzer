package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

public class Jump implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        TraceableWord jumpTo = state.getStack().pop();
        jumpTo.getTrace().addChild(opcode);
        state.setPc(jumpTo.getIntData());
    }
}
