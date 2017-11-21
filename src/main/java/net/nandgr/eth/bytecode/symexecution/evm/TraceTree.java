package net.nandgr.eth.bytecode.symexecution.evm;

import java.util.ArrayList;
import java.util.List;

public class TraceTree<T> {

    private final List<TraceTree> children = new ArrayList<>();
    private final T element;

    public TraceTree(T element) {
        this.element = element;
    }

    public void addChild(TraceTree child) {
        children.add(child);
    }

    public void addChild(T child) {
        children.add(new TraceTree<>(child));
    }
}
