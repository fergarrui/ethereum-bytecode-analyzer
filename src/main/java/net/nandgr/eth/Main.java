package net.nandgr.eth;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.diagram.DotDiagram;
import net.nandgr.eth.exceptions.GraphException;
import net.nandgr.eth.exceptions.ReportException;
import net.nandgr.eth.report.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // TODO check parameters
    public static void main(String[] args) {
        // if bytecode is provided as input...
        if (args.length < 1) {
            return;
        }
        ContractAnalyzer contractAnalyzer = new ContractAnalyzer(args[0]);
        contractAnalyzer.analyzeContract();
        Map<EVMEnvironment, EVMState> executionsStates = contractAnalyzer.getExecutionsStates();
        Map<Integer, BytecodeChunk> chunks = contractAnalyzer.getChunks();

        System.out.println("===== CREATING REPORT =======");
        Report report = new Report(contractAnalyzer);
        try {
            report.createReport();
        } catch (ReportException e) {
            e.printStackTrace();
        }

//        logger.info("========= SUMMARY =========");
//        for (Map.Entry<EVMEnvironment, EVMState> entry : executionsStates.entrySet()) {
//            EVMEnvironment environment = entry.getKey();
//            EVMState state = entry.getValue();
//            logger.info("=-=-=- ENVIRONMENT: " + environment.toString());
//            logger.info("=-=-=- STATE: " + state.printEVMState());
//            logger.debug("Generating graph for environment... " + environment.toString());
//            // generate diagrams
////            try {
////                DotDiagram.buildDotFile(chunks, Collections.emptyList(), "graph", "png");
////            } catch (GraphException e) {
////                 TODO -
////                e.printStackTrace();
////            }
//        }
    }
}
