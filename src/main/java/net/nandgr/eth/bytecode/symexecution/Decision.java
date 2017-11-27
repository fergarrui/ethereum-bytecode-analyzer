package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;

public class Decision {

    private final TraceableWord conditionWord;

    public Decision(TraceableWord conditionWord) {
        this.conditionWord = conditionWord;
    }

    public TraceableWord getConditionWord() {
        return conditionWord;
    }
}
