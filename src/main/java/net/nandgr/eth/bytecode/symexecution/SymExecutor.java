package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.opcodes.OpcodeExecutor;
import net.nandgr.eth.bytecode.symexecution.evm.opcodes.OpcodeExecutors;
import net.nandgr.eth.exceptions.EVMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Executes in an isolated EVM the given bytecode.
 * It records the execution of every opcode.
 * Runs in a new thread.
 */
public class SymExecutor implements Callable<EVMState> {

    private static final Logger logger = LoggerFactory.getLogger(SymExecutor.class);
    /**
     * State of the EVM.
     * Contains the stack, memory, storage, events, environment and chunks
     */
    private final EVMState state;
    /**
     * Holds the opcodes that the program executed.
     * Maybe some paths of the program won't be covered on this
     * execution but are going to be covered in subsequents runs
     * with different inputs/environment
     * LinkedHashMap because insertion order matters when iterating over it
     */
    private final Map<Opcode, EVMState> programExecution = new LinkedHashMap<>();

    public SymExecutor(Map<Integer, BytecodeChunk> chunks, EVMEnvironment evmEnvironment, DecisionsService decisionsService) {
        state = new EVMState(chunks, evmEnvironment, decisionsService);
    }

    public EVMState getState() {
        return state;
    }

    public Map<Opcode, EVMState> getProgramExecution() {
        return programExecution;
    }

    @Override
    public EVMState call() throws EVMException {
        return execute();
    }

    public EVMState execute() throws EVMException {
        if (state.getChunks().isEmpty()) {
            String message = "Provided bytecode chunks empty";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        int chunksOffset = 0;
        BytecodeChunk bytecodeChunk = state.getChunks().get(chunksOffset);

        while (state.isRunning()) {
            logger.info("SymExecutor loops");
            int pc = state.getPc();
            if (pc != chunksOffset) {
                if(!state.getChunks().containsKey(pc)) {
                    logger.error("Program counter: {} does not match with any existing chunk", pc);
                    throw new EVMException("Program counter " + pc + " does not match with any existing chunk");
                }
                bytecodeChunk = state.getChunks().get(pc);
                chunksOffset = pc;
            } // else : machine should be stopped
            for (Opcode opcode : bytecodeChunk.getOpcodes()) {
                OpcodeExecutor executor = OpcodeExecutors.findExecutor(opcode);
                executor.execute(state, opcode);
                programExecution.put(opcode, new EVMState(state));
                logger.info("Executed opcode {} with {}", opcode, executor.getClass());
                logger.debug("EVM State: {}", state.printEVMState());
            }
        }
        return state;
    }
}
