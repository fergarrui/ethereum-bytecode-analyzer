package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.evm.opcodes.OpcodeUtils;
import net.nandgr.eth.exceptions.TraceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EQTrace implements TraceAnalyzer {

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

        if (isChildren0Symbolic) {
            List<TraceableWord> output = children1.getExecutionTrace().getOutput();
            calculate(children0.getExecutionTrace().getSymbolicTransformation());
        } else {

        }

        // TODO -
        return null;

    }

    private void calculate(SymbolicTransformation symbolicTransformation) {
//        symbolicTransformation.getTransformations().get(0).
//        if (OpcodeUtils.isSymbolic(symbolicTransformation.getOperation().getOpcode())) {
//
//        }
//        if (symbolicTransformation.getTransformations().isEmpty()) {
//            symbolicTransformation.
//        }
//        List<SymbolicTransformation>  transformations = symbolicTransformation.getTransformations();
//        for (SymbolicTransformation transformation : transformations) {
//            calculate(transformation);
//        }
    }
}
