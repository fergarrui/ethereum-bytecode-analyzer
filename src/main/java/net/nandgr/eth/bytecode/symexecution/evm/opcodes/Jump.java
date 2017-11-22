package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

import java.util.Map;

public class Jump implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) {
        TraceableWord jumpTo = state.getStack().pop();
        jumpTo.getTrace().addChild(opcode);
        Map<Integer, BytecodeChunk> chunks = state.getChunks();
        BytecodeChunk runningChunk = chunks.get(state.getPc());
        if (runningChunk.hasEmptyRelations()) {
            state.addRelation(runningChunk,chunks.get(jumpTo.getIntData()), null);
        }
        state.setPc(jumpTo.getIntData());
    }
}
