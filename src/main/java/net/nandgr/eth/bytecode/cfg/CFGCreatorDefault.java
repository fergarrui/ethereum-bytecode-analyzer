package net.nandgr.eth.bytecode.cfg;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.beans.BytecodeSection;
import net.nandgr.eth.bytecode.beans.ContractBytecode;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CFGCreatorDefault implements CFGCreator {

    private final static List<Opcodes> endChunkOpcodes = Stream.of(
            Opcodes.JUMPI,
            Opcodes.JUMP,
            Opcodes.STOP,
            Opcodes.REVERT,
            Opcodes.RETURN
    ).collect(Collectors.toList());

    @Override
    public ContractBytecode createContractBytecode(List<Opcode> contractOpcodes) {
        BigInteger codeOffset = BigInteger.valueOf(0);
        for (int i = 0 ; i < contractOpcodes.size() ; i++) {
            Opcode contractOpcode = contractOpcodes.get(i);
            if (contractOpcode.getOpcode().equals(Opcodes.CODECOPY)) {
                codeOffset = contractOpcodes.get(i-2).getParameter();
            }
            if (contractOpcode.getOffset() != 0 && contractOpcode.getOffset() == codeOffset.intValue()) {
                List<Opcode> constructorOpcodes = contractOpcodes.subList(0, i);
                List<Opcode> functionsOpcodes = contractOpcodes.subList(i, contractOpcodes.size()-1);
                adjustOffsets(functionsOpcodes, codeOffset.intValue());
                Map<Integer, BytecodeChunk> constructorChunks = splitInChunks(constructorOpcodes);
                Map<Integer, BytecodeChunk> functionsChunks = splitInChunks(functionsOpcodes);
                createRelations(constructorChunks);
                createRelations(functionsChunks);
                BytecodeSection constructorSection = new BytecodeSection(constructorChunks);
                BytecodeSection functionsSection = new BytecodeSection(functionsChunks);
                return new ContractBytecode(constructorSection, functionsSection);
            }
        }
        return null;
    }

    private static void adjustOffsets(List<Opcode> functionsOpcodes, int offset) {
        for (Opcode opcode : functionsOpcodes) {
            opcode.setOffset(opcode.getOffset() - offset);
        }
    }

    @Override
    public Map<Integer, BytecodeChunk> splitInChunks(List<Opcode> opcodes) {
        Map<Integer, BytecodeChunk> chunks = new HashMap<>();
        int startIndex = 0;
        for (int i = 0 ; i < opcodes.size() ; i++) {
            Opcode opcode = opcodes.get(i);
            if (endChunkOpcodes.contains(opcode.getOpcode())) {
                BytecodeChunk bytecodeChunk = new BytecodeChunk(i);
                List<Opcode> chunkOpcodes = opcodes.subList(startIndex, i + 1);
                bytecodeChunk.setOpcodes(chunkOpcodes);
                if (!chunkOpcodes.isEmpty()) {
                    Opcode jumpDest = getJumpDest(chunkOpcodes);
                    chunks.put(jumpDest.getOffset(), bytecodeChunk);
                }
                startIndex = i + 1;
            }
        }
        return chunks;
    }

    private Opcode getJumpDest(List<Opcode> chunkOpcodes) {
        for (Opcode chunkOpcode : chunkOpcodes) {
            if (chunkOpcode.getOpcode().equals(Opcodes.JUMPDEST)) {
                return chunkOpcode;
            }
        }
        return chunkOpcodes.get(0);
    }

    @Override
    public void createRelations(Map<Integer, BytecodeChunk> chunks) {
        for (Map.Entry<Integer, BytecodeChunk> chunkEntry : chunks.entrySet()) {
            BytecodeChunk chunk = chunkEntry.getValue();
            List<Opcode> opcodes = chunk.getOpcodes();
            Opcode lastOpcode = opcodes.get(opcodes.size() - 1);
            Opcodes lastOpcodeDefinition = lastOpcode.getOpcode();
            if (lastOpcodeDefinition.equals(Opcodes.JUMPI) || lastOpcodeDefinition.equals(Opcodes.JUMP)) {
                Opcode previousOpcode = opcodes.get(opcodes.size() -2);
                if (Opcodes.isPush(previousOpcode.getOpcode())) {
                    BigInteger jumpLocation = previousOpcode.getParameter();
                    BytecodeChunk jumpToChunk = chunks.get(jumpLocation.intValue());
                    chunk.setBranchA(jumpToChunk);
                    // If it is not the last chunk
                    int nextKey = lastOpcode.getOffset() + 1;
                    if (lastOpcodeDefinition.equals(Opcodes.JUMPI) && chunks.containsKey(nextKey)) {
                        chunk.setBranchB(chunks.get(nextKey));
                    }
                }
            }
        }
    }
}
