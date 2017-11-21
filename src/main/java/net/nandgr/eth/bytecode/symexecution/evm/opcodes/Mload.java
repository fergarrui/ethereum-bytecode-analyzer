package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import java.util.Map;
import java.util.Stack;

public class Mload implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        Map<Integer, TraceableWord> memory = state.getMemory().getMemory();
        TraceableWord memoryIndex = stack.pop();

        TraceTree<Opcode> trace = memoryIndex.getTrace();
        TraceTree<Opcode> traceTree = new TraceTree<>(opcode);
        trace.addChild(traceTree);

        TraceableWord traceableWord = memory.get(memoryIndex.getIntData());
        traceableWord.getTrace().addChild(traceTree);

        stack.push(traceableWord);
    }
}
