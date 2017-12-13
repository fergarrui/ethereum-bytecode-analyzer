package net.nandgr.eth.bytecode.symexecution.evm;

import java.util.HashMap;
import java.util.Map;

public class EVMStorage {

    private final Map<TraceableWord, TraceableWord> storage = new HashMap<>();

    public EVMStorage() {
    }

    public EVMStorage(EVMStorage storage) {
        this.storage.putAll(storage.getStorage());
    }

    public Map<TraceableWord, TraceableWord> getStorage() {
        return storage;
    }

    public TraceableWord put(TraceableWord key, TraceableWord value) {
        return storage.put(key, value);
    }

    public TraceableWord get(TraceableWord key) {
        return storage.get(key);
    }

    public String printStorage() {
        StringBuilder sb = new StringBuilder(System.lineSeparator()).append("!!!!!!!!!!!!!!!!!!!!").append(System.lineSeparator());

        for (Map.Entry<TraceableWord, TraceableWord> entry : storage.entrySet()) {
            sb.append("|");
            sb.append(entry.getKey()).append(" => " ).append(entry.getValue()).append(System.lineSeparator());
            sb.append("!!!!!!!!!!!!!!!!!!!!").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
