package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.evm.TraceTree;
import net.nandgr.eth.exceptions.EVMException;
import java.math.BigInteger;

public class Push implements OpcodeExecutor {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        BigInteger parameter = opcode.getParameter();

        TraceableWord traceableWord = new TraceableWord(parameter.toByteArray(), new TraceTree(opcode));
        state.getStack().push(traceableWord);
    }
}
