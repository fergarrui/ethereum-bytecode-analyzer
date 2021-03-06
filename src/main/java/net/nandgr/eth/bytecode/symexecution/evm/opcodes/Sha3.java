package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.ethereumjcrypto.HashUtil;
import net.nandgr.eth.exceptions.EVMException;
import net.nandgr.eth.utils.Lists;
import java.util.Map;

public class Sha3 extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        Map<Integer, TraceableWord> memory = state.getMemory().getMemory();
        TraceableWord memoryIndexWord = stack.pop();
        TraceableWord lengthWord = stack.pop();
        int length = lengthWord.getIntData();
        if (length % TraceableWord.WORD_SIZE != 0) {
            throw new UnsupportedOperationException("Length that is not word size is not supported at the moment");
        }

        int numberOfWords = length / TraceableWord.WORD_SIZE;
        byte[] bytesToHash = new byte[0];
        int memIndex = memoryIndexWord.getIntData();

        for (int i = 0; i < numberOfWords; i++) {
            TraceableWord traceableWord = memory.get(memIndex);
            // TODO need to trace this ?
//            traceableWord.getTrace().addChild(traceTree);
//            traceTree.addChild(traceableWord.getTrace());
            byte[] bytes = traceableWord.getBytes32();
            bytesToHash = concatenate(bytesToHash, bytes);
            memIndex += TraceableWord.WORD_SIZE;
        }
        byte[] hashedBytes = HashUtil.sha3(bytesToHash);
        TraceableWord hashedWord = new TraceableWord(hashedBytes);
        TraceTree traceTree = buildTraceTree(opcode, hashedWord, Lists.of(memoryIndexWord, lengthWord));
        traceTree.addChild(memoryIndexWord.getTrace());
        traceTree.addChild(lengthWord.getTrace());
        memoryIndexWord.getTrace().addChild(traceTree);
        lengthWord.getTrace().addChild(traceTree);
        hashedWord.setTrace(traceTree);
        stack.push(hashedWord);
    }

    private static byte[] concatenate(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
