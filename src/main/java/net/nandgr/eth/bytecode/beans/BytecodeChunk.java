package net.nandgr.eth.bytecode.beans;

import net.nandgr.eth.Opcode;

import java.util.ArrayList;
import java.util.List;

public class BytecodeChunk {

    private final int id;
    private BytecodeChunk branchA;
    private BytecodeChunk branchB;
    private List<Opcode> opcodes = new ArrayList<>();

    public BytecodeChunk(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public BytecodeChunk getBranchA() {
        return branchA;
    }

    public void setBranchA(BytecodeChunk branchA) {
        this.branchA = branchA;
    }

    public BytecodeChunk getBranchB() {
        return branchB;
    }

    public void setBranchB(BytecodeChunk branchB) {
        this.branchB = branchB;
    }

    public List<Opcode> getOpcodes() {
        return opcodes;
    }

    public void setOpcodes(List<Opcode> opcodes) {
        this.opcodes = opcodes;
    }

    @Override
    public String toString() {
        return "BytecodeChunk{" +
                "id=" + id +
                ", branchA=" + branchA +
                ", branchB=" + branchB +
                ", opcodes=" + opcodes +
                '}';
    }
}
