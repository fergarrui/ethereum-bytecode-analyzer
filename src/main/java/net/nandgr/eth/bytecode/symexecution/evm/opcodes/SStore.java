package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.*;
import net.nandgr.eth.exceptions.EVMException;

public class SStore implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();

        TraceableWord key = stack.pop();
        TraceableWord value = stack.pop();

        TraceTree<Opcode> traceTree = new TraceTree<>(opcode);
        key.getTrace().addChild(traceTree);
        value.getTrace().addChild(traceTree);

        storage.put(key, value);
    }
}
