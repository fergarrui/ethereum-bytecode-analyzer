package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import java.util.List;
import java.util.Stack;

public class CallDataLoad implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        List<Byte> callData = state.getEvmEnvironment().getCallData();
        EVMStack stack = state.getStack();

        TraceableWord dataIndex = stack.pop();
        dataIndex.getTrace().addChild(opcode);
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

        TraceableWord word = new TraceableWord(bytes, new TraceTree<>(opcode));
        stack.push(word);
    }
}
