package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.*;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

public class SStore extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();

        TraceableWord key = stack.pop();
        TraceableWord value = stack.pop();

        TraceTree traceTree = buildTraceTree(opcode, null, Lists.of(key, value));
        key.getTrace().addChild(traceTree);
        value.getTrace().addChild(traceTree);

        storage.put(key, value);
    }
}
