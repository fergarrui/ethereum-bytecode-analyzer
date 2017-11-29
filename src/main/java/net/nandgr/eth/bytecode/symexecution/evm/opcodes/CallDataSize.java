package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class CallDataSize extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        List<Byte> callData = state.getEvmEnvironment().getCallData();
        int callDataSize = callData.size();
        TraceableWord traceableWord = new TraceableWord(BigInteger.valueOf(callDataSize).toByteArray());
        TraceTree traceTree = buildTraceTree(opcode,traceableWord, Collections.emptyList());
        traceableWord.setTrace(traceTree);
        state.getStack().push(traceableWord);
    }
}
