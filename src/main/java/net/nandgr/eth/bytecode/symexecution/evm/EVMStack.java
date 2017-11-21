package net.nandgr.eth.bytecode.symexecution.evm;

import java.util.Stack;

public class EVMStack {

    private final Stack<TraceableWord> stack = new Stack<>();

    public TraceableWord push(TraceableWord item) {
        return stack.push(item);
    }

    public TraceableWord pop() {
        return stack.pop();
    }

    public int size() {
        return stack.size();
    }

    public TraceableWord get(int index) {
        return stack.get(index);
    }

    public void insertElementAt(TraceableWord element, int index) {
        stack.insertElementAt(element, index);
    }

    public TraceableWord remove(int index) {
        return stack.remove(index);
    }

    public String printStack() {
        StringBuilder sb = new StringBuilder(System.lineSeparator()).append("####################").append(System.lineSeparator());
        for (TraceableWord traceableWord : stack) {
            sb.append("|");
            sb.append(traceableWord.toString()).append(System.lineSeparator());
            sb.append("####################").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
