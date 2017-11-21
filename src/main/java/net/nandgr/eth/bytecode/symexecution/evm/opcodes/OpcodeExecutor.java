package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.exceptions.EVMException;

public interface OpcodeExecutor {
    void execute(EVMState state, Opcode opcode) throws EVMException;
}
