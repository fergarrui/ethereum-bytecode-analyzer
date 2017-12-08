package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.trace.revert.*;
import net.nandgr.eth.exceptions.TraceException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAnalyzer implements TraceAnalyzer {

    private static Map<Opcodes, OperationRevert> operationRevertMap = new HashMap<>();

    static {
        operationRevertMap.put(Opcodes.ADD, new AddRevert());
        operationRevertMap.put(Opcodes.SUB, new SubRevert());
        operationRevertMap.put(Opcodes.MUL, new MulRevert());
        operationRevertMap.put(Opcodes.DIV, new DivRevert());
        operationRevertMap.put(Opcodes.XOR, new XorRevert());
        operationRevertMap.put(Opcodes.NOT, new NotRevert());
        operationRevertMap.put(Opcodes.AND, new AndRevert());
    }

    @Override
    public abstract EVMEnvironment createEnvironmentForTrace(TraceTree trace, EVMEnvironment environment) throws TraceException;

    protected static OperationRevert findRevert(Opcodes opcode) throws TraceException {
        if (!operationRevertMap.containsKey(opcode)) {
            throw new TraceException("Operation : " + opcode + ", cannot be reverted");
        }
        return operationRevertMap.get(opcode);
    }
}
