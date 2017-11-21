package net.nandgr.eth.bytecode.symexecution.evm;

import net.nandgr.eth.Opcode;

public interface Traceable {

    TraceTree<Opcode> getTrace();
}
