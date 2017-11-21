package net.nandgr.eth.bytecode.cfg;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.ContractBytecode;
import java.util.List;
import java.util.Map;

public interface CFGCreator {
    ContractBytecode createContractBytecode(List<Opcode> contractOpcodes);
    Map<Integer, BytecodeChunk> splitInChunks(List<Opcode> opcodes);
    void createRelations(Map<Integer, BytecodeChunk> chunks);
}
