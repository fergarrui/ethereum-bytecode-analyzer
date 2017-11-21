package net.nandgr.eth.bytecode.symexecution.evm;

import java.util.HashMap;
import java.util.Map;

public class EVMMemory {

    private final Map<Integer, TraceableWord> memory = new HashMap<>();

    public String printMemory() {
        StringBuilder sb = new StringBuilder(System.lineSeparator()).append("********************").append(System.lineSeparator());
        for (Map.Entry<Integer, TraceableWord> wordEntry : memory.entrySet()) {
            sb.append("|");
            sb.append("[0x" + String.format("%03X", wordEntry.getKey())).append("]*> ");
            sb.append(wordEntry.getValue()).append(System.lineSeparator());
            sb.append("********************").append(System.lineSeparator());
        }
        return sb.toString();
    }

    public Map<Integer, TraceableWord> getMemory() {
        return memory;
    }
}
