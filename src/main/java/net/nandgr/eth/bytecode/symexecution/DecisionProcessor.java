package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.trace.comparisons.EQTraceAnalyzer;
import net.nandgr.eth.bytecode.symexecution.trace.IsZeroTrace;
import net.nandgr.eth.bytecode.symexecution.trace.comparisons.LTTraceAnalyzer;
import net.nandgr.eth.bytecode.symexecution.trace.TraceAnalyzer;
import net.nandgr.eth.exceptions.TraceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

class DecisionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DecisionProcessor.class);
    private static final Map<Opcodes, TraceAnalyzer> traceAnalyzers = new HashMap<>();

    static {
        // TODO : Move to a factory class that instantiates the trace analyzers
        // TODO : instead of caching all of them here.
        // Analyzers
        traceAnalyzers.put(Opcodes.ISZERO, new IsZeroTrace());
        traceAnalyzers.put(Opcodes.LT, new LTTraceAnalyzer());
        traceAnalyzers.put(Opcodes.EQ, new EQTraceAnalyzer());
    }

    EVMEnvironment buildEnvironmentFromDecision(Decision decision, EVMEnvironment environment) {
        TraceableWord conditionWord = decision.getConditionWord();
        ExecutionTrace executionTrace = conditionWord.getTrace().getExecutionTrace();
        logger.info("Processing decision for " + conditionWord + ", " + executionTrace);
        TraceAnalyzer traceAnalyzer = traceAnalyzers.get(executionTrace.getOpcode().getOpcode());
        if (traceAnalyzer == null) {
            logger.warn("Unknown condition opcode: " + executionTrace.getOpcode());
            return null;
        }
        try {
            return traceAnalyzer.createEnvironmentForTrace(decision.getConditionWord().getTrace(), environment);
        } catch (TraceException e) {
            logger.error("Error creating environment", e);
        }
        return null;
    }
}
