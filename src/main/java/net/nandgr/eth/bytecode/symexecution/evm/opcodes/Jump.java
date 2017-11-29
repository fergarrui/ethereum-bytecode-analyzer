package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.utils.Lists;

import java.util.Map;

public class Jump extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        TraceableWord jumpTo = state.getStack().pop();
        TraceTree traceTree = buildTraceTree(opcode, null, Lists.of(jumpTo));
        jumpTo.getTrace().addChild(traceTree);
        Map<Integer, BytecodeChunk> chunks = state.getChunks();
        BytecodeChunk runningChunk = chunks.get(state.getPc());
        if (runningChunk.hasEmptyRelations()) {
            state.addRelation(runningChunk,chunks.get(jumpTo.getIntData()), null);
        }
        state.setPc(jumpTo.getIntData());
    }
}
