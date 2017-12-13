package net.nandgr.eth;

import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import net.nandgr.eth.bytecode.cfg.CFGCreator;
import net.nandgr.eth.bytecode.cfg.CFGCreatorDefault;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import java.util.HashMap;
import java.util.Map;

public class ContractAnalyzer {

    private final String code;
    private Map<Integer, BytecodeChunk> chunks = new HashMap<>();
    private Map<EVMEnvironment, EVMState> executionsStates  = new HashMap<>();
    private Map<EVMEnvironment, SymExecutor> executions = new HashMap<>();

    public ContractAnalyzer(String code) {
        this.code = code;
    }

    public void analyzeContract() {
        Disassembler disassembler = new Disassembler(code);
        CFGCreator cfgCreator = new CFGCreatorDefault();
        ContractBytecode contractBytecode = cfgCreator.createContractBytecode(disassembler.getOpcodes());
        chunks = contractBytecode.getFunctionsSection().getChunks();
        SymbolicPathsHandler symbolicPathsHandler = new SymbolicPathsHandler(chunks);
        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();
        executions = symbolicPathsHandler.getExecutions();
        executionsStates = symbolicPathsHandler.getExecutionsStates();
    }

    public String getCode() {
        return code;
    }

    public Map<Integer, BytecodeChunk> getChunks() {
        return chunks;
    }

    public Map<EVMEnvironment, EVMState> getExecutionsStates() {
        return executionsStates;
    }

    public Map<EVMEnvironment, SymExecutor> getExecutions() {
        return executions;
    }
}
