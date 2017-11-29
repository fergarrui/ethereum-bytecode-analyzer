package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;

public class Pop extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        state.getStack().pop();
    }
}
