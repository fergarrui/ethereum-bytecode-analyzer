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
import java.util.Map;

public class SymExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SymExecutor.class);
    private final Map<Integer, BytecodeChunk> chunks;
    private final EVMState state;

    public SymExecutor(Map<Integer, BytecodeChunk> chunks, EVMEnvironment evmEnvironment) {
        this.chunks = chunks;
        state = new EVMState(evmEnvironment);
    }

    public EVMState getState() {
        return state;
    }

    public void execute() throws EVMException {

        if (chunks.isEmpty()) {
            String message = "Provided bytecode chunks empty";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        int chunksOffset = 0;
        BytecodeChunk bytecodeChunk = chunks.get(chunksOffset);

        while (state.isRunning()) {
            logger.info("SymExecutor loops");
            int pc = state.getPc();
            if (pc != chunksOffset) {
                if(!chunks.containsKey(pc)) {
                    logger.error("Program counter: {} does not match with any existing chunk", pc);
                    throw new EVMException("Program counter " + pc + " does not match with any existing chunk");
                }
                bytecodeChunk = chunks.get(pc);
                chunksOffset = pc;
            } // else : machine should be stopped
            for (Opcode opcode : bytecodeChunk.getOpcodes()) {
                OpcodeExecutor executor = OpcodeExecutors.findExecutor(opcode);
                executor.execute(state, opcode);
                logger.info("Executed opcode {} with {}", opcode, executor.getClass());
                logger.debug("EVM State: {}", state.printEVMState());
            }
        }
    }
}
