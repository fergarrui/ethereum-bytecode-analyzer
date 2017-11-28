package net.nandgr.eth.bytecode.symexecution;

import java.util.ArrayList;
import java.util.List;

public class TraceTree {

    private final List<TraceTree> children = new ArrayList<>();
    private final ExecutionTrace executionTrace;

    public TraceTree(ExecutionTrace executionTrace) {
        this.executionTrace = executionTrace;
    }

    public void addChild(TraceTree child) {
        children.add(child);
    }

    public List<TraceTree> getChildren() {
        return children;
    }

    public ExecutionTrace getExecutionTrace() {
        return executionTrace;
    }
}
