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
    private static final List<Opcodes> opcodesInputs = new ArrayList<>();

    static {
        // TODO : Move to a factory class that instantiates the trace analyzers
        // TODO : instead of caching all of them here.
        // Analyzers
        traceAnalyzers.put(Opcodes.ISZERO, new IsZeroTrace());
        traceAnalyzers.put(Opcodes.LT, new LTTrace());
        traceAnalyzers.put(Opcodes.EQ, new EQTrace());
        // Inputs
        opcodesInputs.add(Opcodes.CALLVALUE);
        opcodesInputs.add(Opcodes.CALLDATASIZE);
        opcodesInputs.add(Opcodes.CALLDATALOAD);
        // ??
        opcodesInputs.add(Opcodes.GAS);
        //
        opcodesInputs.add(Opcodes.ADDRESS);
        opcodesInputs.add(Opcodes.BALANCE);
        opcodesInputs.add(Opcodes.ORIGIN);
        opcodesInputs.add(Opcodes.CALLER);
        opcodesInputs.add(Opcodes.CALLVALUE);
        opcodesInputs.add(Opcodes.CALLDATALOAD);
        opcodesInputs.add(Opcodes.CALLDATASIZE);
        opcodesInputs.add(Opcodes.CALLDATACOPY);
        opcodesInputs.add(Opcodes.CODESIZE);
        opcodesInputs.add(Opcodes.CODECOPY);
        opcodesInputs.add(Opcodes.GASPRICE);
        opcodesInputs.add(Opcodes.EXTCODESIZE);
        opcodesInputs.add(Opcodes.EXTCODECOPY);
        opcodesInputs.add(Opcodes.BLOCKHASH);
        opcodesInputs.add(Opcodes.COINBASE);
        opcodesInputs.add(Opcodes.TIMESTAMP);
        opcodesInputs.add(Opcodes.NUMBER);
        opcodesInputs.add(Opcodes.DIFFICULTY);
        opcodesInputs.add(Opcodes.GASLIMIT);
    }

    EVMEnvironment buildEnvironmentFromDecision(Decision decision, EVMEnvironment environment) {
        TraceableWord conditionWord = decision.getConditionWord();
        Opcode conditionElement = conditionWord.getTrace().getElement();
        logger.info("Processing decision for " + conditionWord + ", " + conditionElement);
        TraceAnalyzer traceAnalyzer = traceAnalyzers.get(conditionElement.getOpcode());
        if (traceAnalyzer == null) {
            logger.warn("Unknown condition opcode: " + conditionElement.getOpcode());
            return null;
        }
        List<TraceTree> inputs = findInputs(conditionWord.getTrace());
        logger.info("XXXXX" + Arrays.toString(inputs.toArray()));
        return new EVMEnvironment.EVMEnvironmentBuilder()
                .build();
    }

    private static List<TraceTree> findInputs(TraceTree word) {
        List<TraceTree> inputs = new ArrayList<>();
        for (TraceTree traceTree : word.getChildren()) {
            Opcode element = traceTree.getElement();
            if (opcodesInputs.contains(element.getOpcode())) {
                inputs.add(traceTree);
            }
            inputs.addAll(findInputs(traceTree));
        }
        return inputs;
    }
}
