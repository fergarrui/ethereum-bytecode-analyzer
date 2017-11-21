package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import java.math.BigInteger;
import java.util.List;

public class CallDataSize implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        List<Byte> callData = state.getEvmEnvironment().getCallData();
        int callDataSize = callData.size();
        TraceableWord traceableWord = new TraceableWord(BigInteger.valueOf(callDataSize).toByteArray(), new TraceTree<>(opcode));
        state.getStack().push(traceableWord);
    }
}
