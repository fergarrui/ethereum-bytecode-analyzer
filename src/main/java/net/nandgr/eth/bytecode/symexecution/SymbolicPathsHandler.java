package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static net.nandgr.eth.Parameters.MAX_THREADS;

public class SymbolicPathsHandler implements Subscriber<Decision> {

    private final Map<Integer, BytecodeChunk> chunks;
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
    private final DecisionProcessor decisionProcessor = new DecisionProcessor();

    public SymbolicPathsHandler(Map<Integer, BytecodeChunk> chunks) {
        this.chunks = chunks;
    }

    @Override
    public void inform(Decision element) {
        EVMEnvironment evmEnvironment = decisionProcessor.buildEnvironmentFromDecision(element);
        // TODO if environment not tested already ...
        SymExecutor symExecutor = new SymExecutor(chunks, evmEnvironment);

    }
}
