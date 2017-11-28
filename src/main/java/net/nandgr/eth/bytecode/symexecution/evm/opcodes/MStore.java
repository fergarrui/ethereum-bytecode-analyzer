package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import java.math.BigInteger;
import java.util.Map;

public class MStore implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        Map<Integer, TraceableWord> memory = state.getMemory().getMemory();
        TraceableWord memoryIndex = stack.pop();
        TraceableWord memoryContent = stack.pop();

        // maybe wrap Opcode and add a type for some opcodes,
        // the role in the execution could be saved, e.g.
        // - was used for the index
        // - was used to store it in memory
        TraceTree trace1 = memoryIndex.getTrace();
        TraceTree trace2 = memoryContent.getTrace();
        TraceTree traceTree = new TraceTree(opcode);
        trace1.addChild(traceTree);
        trace2.addChild(traceTree);

        BigInteger index = new BigInteger(memoryIndex.getBytes());
        memory.put(index.intValue(), memoryContent);
    }
}
