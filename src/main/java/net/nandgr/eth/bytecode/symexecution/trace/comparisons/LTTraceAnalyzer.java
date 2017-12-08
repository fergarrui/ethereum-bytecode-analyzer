package net.nandgr.eth.bytecode.symexecution.trace.comparisons;

import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.trace.ProcessChildResult;
import net.nandgr.eth.exceptions.TraceException;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.List;

public class LTTraceAnalyzer extends AbstractComparisonAnalyzer {

    @Override
    protected EVMEnvironment createEVMEnvironmentForResult(ProcessChildResult result, List<TraceableWord> output, EVMEnvironment environment) throws TraceException {
        if (output.size() != 1) {
            throw new TraceException("Output of the non symbolic child must have size equals to 1");
        }
        EVMEnvironment.EVMEnvironmentBuilder builder = new EVMEnvironment.EVMEnvironmentBuilder();
        Opcodes resultSymbolicOperation = result.getOpcode();
        if (resultSymbolicOperation.equals(Opcodes.CALLDATALOAD)) {
            BigInteger r = result.getResult();
            builder.setCallData(r.add(BigInteger.ONE).toByteArray());
        } else if(resultSymbolicOperation.equals(Opcodes.CALLDATASIZE)) {
            if(environment.getCallData().isEmpty()) {
                TraceableWord outputWord = output.get(0);
                int intData = outputWord.getIntData() + 1;
                byte[] callData = new byte[intData];
                for (int i = 0; i < intData ; i++) {
                    callData[i] = 1;
                }
                builder.setCallData(callData);
            } else {
                builder.setCallData(ArrayUtils.toPrimitive((Byte[]) environment.getCallData().toArray()));
            }
        }
        return builder.build();
    }
}
