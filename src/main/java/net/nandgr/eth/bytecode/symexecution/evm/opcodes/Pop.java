package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;

public class Pop implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        state.getStack().pop();
    }
}
