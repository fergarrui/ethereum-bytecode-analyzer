package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;

public class Nop implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        // nop
    }
}
