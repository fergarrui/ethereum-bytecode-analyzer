package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.*;
import net.nandgr.eth.exceptions.EVMException;

public class SLoad implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();

        TraceableWord key = stack.pop();

        TraceTree<Opcode> traceTree = new TraceTree<>(opcode);
        key.getTrace().addChild(traceTree);

        TraceableWord value = storage.get(key);
        value.getTrace().addChild(traceTree);

        stack.push(value);
    }
}
