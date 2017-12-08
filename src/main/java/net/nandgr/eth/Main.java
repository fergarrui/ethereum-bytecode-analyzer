package net.nandgr.eth;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.diagram.DotDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // TODO check parameters
    public static void main(String[] args) {
        // if bytecode is provided as input...
        if (args.length < 1) {
            return;
        }
        Disassembler disassembler = new Disassembler(args[0]);
        CFGCreator cfgCreator = new CFGCreatorDefault();
        ContractBytecode contractBytecode = cfgCreator.createContractBytecode(disassembler.getOpcodes());
        Map<Integer, BytecodeChunk> chunks = contractBytecode.getFunctionsSection().getChunks();
        SymbolicPathsHandler symbolicPathsHandler = new SymbolicPathsHandler(chunks);
        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();
        Map<EVMEnvironment, EVMState> executionsStates = symbolicPathsHandler.getExecutionsStates();
        logger.info("========= SUMMARY =========");
        for (Map.Entry<EVMEnvironment, EVMState> entry : executionsStates.entrySet()) {
            EVMEnvironment environment = entry.getKey();
            EVMState state = entry.getValue();
            logger.info("=-=-=- ENVIRONMENT: " + environment.toString());
            logger.info("=-=-=- STATE: " + state.printEVMState());
            logger.debug("Generating graph for environment... " + environment.toString());
            // generate diagrams
            String diagram = DotDiagram.buildDotFormat(state.getChunks());
            logger.debug(diagram);
        }
    }
}
