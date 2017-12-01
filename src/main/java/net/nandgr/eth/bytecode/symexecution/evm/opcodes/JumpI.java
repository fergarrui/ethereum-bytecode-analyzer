package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.symexecution.Decision;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;

public class JumpI extends AbstractOpcode {

    @Override
    public void execute(EVMState state, Opcode opcode) throws EVMException {
        EVMStack stack = state.getStack();
        TraceableWord jumpTo = stack.pop();
        TraceableWord jumpIf = stack.pop();
        if (jumpIf.getIntData() != 0) {
            int jumpToInt = jumpTo.getIntData();
            if (!state.getChunks().containsKey(jumpToInt)) {
                String errorMessage = "The given destination does not exist";
                logger.error(errorMessage);
                throw new EVMException(errorMessage);
            }
            state.setPc(jumpToInt);
        } else {
            state.setPc(opcode.getOffset() + 1);
        }
        state.getDecisionsService().addDecision(new Decision(jumpIf), state.getEvmEnvironment());
    }
}
