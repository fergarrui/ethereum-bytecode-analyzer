package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.trace.EQTrace;
import net.nandgr.eth.bytecode.symexecution.trace.IsZeroTrace;
import net.nandgr.eth.bytecode.symexecution.trace.LTTrace;
import net.nandgr.eth.bytecode.symexecution.trace.TraceAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

class DecisionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DecisionProcessor.class);
    private static final Map<Opcodes, TraceAnalyzer> traceAnalyzers = new HashMap<>();

    static {
        // TODO : Move to a factory class that instantiates the trace analyzers
        // TODO : instead of caching all of them here.
        // Analyzers
        traceAnalyzers.put(Opcodes.ISZERO, new IsZeroTrace());
        traceAnalyzers.put(Opcodes.LT, new LTTrace());
        traceAnalyzers.put(Opcodes.EQ, new EQTrace());
    }

    EVMEnvironment buildEnvironmentFromDecision(Decision decision, EVMEnvironment environment) {
        TraceableWord conditionWord = decision.getConditionWord();
        ExecutionTrace executionTrace = conditionWord.getTrace().getExecutionTrace();
        logger.info("Processing decision for " + conditionWord + ", " + executionTrace);
        TraceAnalyzer traceAnalyzer = traceAnalyzers.get(executionTrace.getOpcode());
        if (traceAnalyzer == null) {
            logger.warn("Unknown condition opcode: " + executionTrace.getOpcode());
            return null;
        }
        return new EVMEnvironment.EVMEnvironmentBuilder()
                .build();
    }
}
