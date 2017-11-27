package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import static net.nandgr.eth.Parameters.MAX_THREADS;

/**
 * Entry point.
 * It starts an execution with a default environment. When the execution detects
 * decisions, a new environment will be created
 */
public class SymbolicPathsHandler implements Subscriber<Decision, EVMEnvironment> {

    private static final Logger logger = LoggerFactory.getLogger(SymbolicPathsHandler.class);

    private final Map<Integer, BytecodeChunk> chunks;
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
    private final DecisionProcessor decisionProcessor = new DecisionProcessor();
    private final DecisionsService decisionsService = new DecisionsService();
    private final Map<EVMEnvironment, SymExecutor> executions = new HashMap<>();
    private final Map<EVMEnvironment, EVMState> executionsStates = new HashMap<>();
    private final AtomicInteger numberOfTasks = new AtomicInteger(0);

    public SymbolicPathsHandler(Map<Integer, BytecodeChunk> chunks) {
        this.chunks = chunks;
    }

    public void startSymbolicExecution() {
        startSymbolicExecution(null);
    }

    public void startSymbolicExecution(EVMEnvironment defaultEnvironment) {
        decisionsService.subscribe(this);
        if (defaultEnvironment == null) {
            defaultEnvironment = new EVMEnvironment.EVMEnvironmentBuilder().build();
        }
        SymExecutor symExecutor = new SymExecutor(chunks, defaultEnvironment, decisionsService);
        executions.put(defaultEnvironment, symExecutor);
        Future<EVMState> submit = executorService.submit(symExecutor);
        numberOfTasks.incrementAndGet();
        try {
            EVMState evmState = submit.get();
            executionsStates.put(defaultEnvironment, evmState);
            numberOfTasks.decrementAndGet();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Execution Interrupted" , e);
        }
    }

    @Override
    public void inform(Decision element, EVMEnvironment environment) {
        logger.info("New decision added : " + element.getConditionWord());
        EVMEnvironment evmEnvironment = decisionProcessor.buildEnvironmentFromDecision(element, environment);
        if (evmEnvironment != null && !executions.containsKey(evmEnvironment)) {
            SymExecutor symExecutor = new SymExecutor(chunks, evmEnvironment, decisionsService);
            executions.put(evmEnvironment, symExecutor);
            Future<EVMState> submit = executorService.submit(symExecutor);
            numberOfTasks.incrementAndGet();
            try {
                EVMState evmState = submit.get();
                executionsStates.put(evmEnvironment, evmState);
                numberOfTasks.decrementAndGet();
            } catch (ExecutionException | InterruptedException e) {
                logger.error("Execution Interrupted" , e);
            }
        } else {
            logger.warn("Environment run already. Skipping." + evmEnvironment);
        }
    }

    public boolean await() {
        while (numberOfTasks.intValue() != 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Interrupted while checking if execution finished");
            }
        }
        return true;
    }

    public Map<EVMEnvironment, EVMState> getExecutionsStates() {
        return executionsStates;
    }
}
