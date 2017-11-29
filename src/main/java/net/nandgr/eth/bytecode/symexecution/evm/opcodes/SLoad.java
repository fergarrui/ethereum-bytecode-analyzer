package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.*;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

public class SLoad extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        EVMStorage storage = state.getStorage();

        TraceableWord key = stack.pop();

        TraceTree traceTree = buildTraceTree(opcode, null, Lists.of(key));
        key.getTrace().addChild(traceTree);

        TraceableWord value = storage.get(key);
        if (value != null) {
            value.getTrace().addChild(traceTree);
        } else {
            value = new TraceableWord(new byte[0]);
            value.setTrace(traceTree);
        }
        stack.push(value);
    }
}
