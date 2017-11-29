package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.util.Map;

public class Mload extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        Map<Integer, TraceableWord> memory = state.getMemory().getMemory();
        TraceableWord memoryIndex = stack.pop();

        TraceTree trace = memoryIndex.getTrace();

        TraceableWord traceableWord = memory.get(memoryIndex.getIntData());
        TraceTree traceTree = buildTraceTree(opcode, traceableWord, Lists.of(memoryIndex));
        trace.addChild(traceTree);
        traceableWord.getTrace().addChild(traceTree);

        stack.push(traceableWord);
    }
}
