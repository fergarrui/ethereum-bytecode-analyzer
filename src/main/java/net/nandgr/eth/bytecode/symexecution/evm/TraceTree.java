package net.nandgr.eth.bytecode.symexecution.evm;

import net.nandgr.eth.Opcode;
import java.util.ArrayList;
import java.util.List;

public class TraceTree {

    private final List<TraceTree> children = new ArrayList<>();
    private final Opcode element;

    public TraceTree(Opcode element) {
        this.element = element;
    }

    public void addChild(TraceTree child) {
        children.add(child);
    }

    public void addChild(Opcode child) {
        children.add(new TraceTree(child));
    }

    public List<TraceTree> getChildren() {
        return children;
    }

    public Opcode getElement() {
        return element;
    }
}
