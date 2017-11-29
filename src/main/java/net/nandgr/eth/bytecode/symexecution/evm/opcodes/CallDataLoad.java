package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;

import java.util.List;

public class CallDataLoad extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        List<Byte> callData = state.getEvmEnvironment().getCallData();
        EVMStack stack = state.getStack();

        TraceableWord dataIndex = stack.pop();

        int index = dataIndex.getIntData();
        if(index > callData.size()) {
            throw new EVMException("Trying to load CALLDATA with an index bigger than the data size");
        }
        byte[] bytes = new byte[32];
        for (int i = 0; i < TraceableWord.WORD_SIZE ; i++) {
            if (index < callData.size()) {
                bytes[i] = callData.get(index);
            } else {
                bytes[i] = 0x0;
            }
            index++;
        }

        TraceableWord word = new TraceableWord(bytes);
        TraceTree traceTree = buildTraceTree(opcode, word, Lists.of(dataIndex));
        word.setTrace(traceTree);
        dataIndex.getTrace().addChild(traceTree);
        stack.push(word);
    }
}
