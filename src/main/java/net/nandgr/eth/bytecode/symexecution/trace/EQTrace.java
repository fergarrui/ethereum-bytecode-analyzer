package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.evm.opcodes.OpcodeUtils;
import net.nandgr.eth.bytecode.symexecution.trace.comparisons.AbstractComparison;
import net.nandgr.eth.exceptions.TraceException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;

public class EQTrace extends AbstractComparison {

    private static final Logger logger = LoggerFactory.getLogger(EQTrace.class);

    @Override
    public EVMEnvironment createEnvironmentForTrace(TraceTree trace) throws TraceException {
        List<TraceTree> children = trace.getChildren();

        // TODO - move to parent class
        if (children.size() != 2) {
            String errorMessage = "Trace must have 2 children and have: " + children.size();
            logger.error(errorMessage);
            throw new TraceException(errorMessage);
        }
        TraceTree children0 = children.get(0);
        TraceTree children1 = children.get(1);

        boolean isChildren0Symbolic = children0.getExecutionTrace().isSymbolic();
        boolean isChildren1Symbolic = children1.getExecutionTrace().isSymbolic();

        if (isChildren0Symbolic && isChildren1Symbolic) {
            String errorMessage = "Decision has 2 symbolic children. Cannot be solved at the moment";
            logger.error(errorMessage);
            throw new TraceException(errorMessage);
        }
        ProcessChildResult result;
        if (isChildren0Symbolic) {
             result = processChild(children0, children1);
        } else {
            result = processChild(children1, children0);
        }
        // TODO -
        Opcodes resultSymbolicOperation = result.getOpcode();
        logger.info("Solved word is: 0x" + Hex.encodeHexString(result.getResult().toByteArray()) + ", for symbol: "+ resultSymbolicOperation);
        EVMEnvironment.EVMEnvironmentBuilder builder = new EVMEnvironment.EVMEnvironmentBuilder();
        if (resultSymbolicOperation.equals(Opcodes.CALLDATALOAD)) {
            builder.setCallData(result.getResult().toByteArray());
        }
        return builder.build();
    }

    private ProcessChildResult processChild(TraceTree symbolicChild, TraceTree constantChild) throws TraceException {
        List<TraceableWord> output = constantChild.getExecutionTrace().getOutput();
        if (output.size() != 1) {
            throw new TraceException("Output must be of size 1 to be able to solve the trace.");
        }
        TraceableWord outputWord = output.get(0);
        BigInteger resultInt = outputWord.getBigInteger();
        SymbolicTransformation symbolicTransformation = symbolicChild.getExecutionTrace().getSymbolicTransformation();
        List<SymbolicTransformation> transformations = symbolicTransformation.getTransformations();
        Opcodes opcodeSymbolic = Opcodes.UNKNOWN;
        while (!transformations.isEmpty() && !OpcodeUtils.isSymbolic(symbolicTransformation.getOperation().getOpcode())) {
            Opcode operation = symbolicTransformation.getOperation();
            // TODO support more than 2 children in the transformations
            // TODO !!! heavy refactor !!!
            SymbolicTransformation tf0 = symbolicTransformation.getTransformations().get(0);
            SymbolicTransformation tf1 = symbolicTransformation.getTransformations().get(1);
            BigInteger constantInt;
            if (tf0.getNumOfInputs() == 0) {
                constantInt = tf0.getConstantValue().getBigInteger();
                transformations = tf1.getTransformations();
                opcodeSymbolic = tf1.getOperation().getOpcode();
                symbolicTransformation = tf1;
            } else {
                constantInt = tf1.getConstantValue().getBigInteger();
                transformations = tf0.getTransformations();
                opcodeSymbolic = tf0.getOperation().getOpcode();
                symbolicTransformation = tf0;
            }
            resultInt = findRevert(operation.getOpcode()).revert(resultInt, constantInt);
        }
        return new ProcessChildResult(resultInt, opcodeSymbolic);
    }
}
