package net.nandgr.eth.diagram;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;

import java.util.List;
import java.util.Map;

public class DotDiagram {

    public static String buildDotFormat(Map<Integer, BytecodeChunk> chunks) {
        StringBuilder sb = new StringBuilder("digraph {").append(System.lineSeparator());
        sb.append("node [shape=box];").append(System.lineSeparator());
        for (BytecodeChunk chunk : chunks.values()) {
            sb.append(chunk.getId() + "[label=\"").append(buildLabelFromChunk(chunk)).append("\"];").append(System.lineSeparator());
            if (chunk.getBranchA() != null) {
                sb.append(chunk.getId()).append("->").append(chunk.getBranchA().getId()).append(";");
            }
            if (chunk.getBranchB() != null) {
                sb.append(chunk.getId()).append("->").append(chunk.getBranchB().getId()).append(";");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String buildLabelFromChunk(BytecodeChunk bytecodeChunk) {
        StringBuilder sb = new StringBuilder();
        for (Opcode opcode : bytecodeChunk.getOpcodes()) {
            sb.append(opcode.toString());
            sb.append("\\l");
        }
        return sb.toString();
    }
}
