package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.exceptions.TraceException;

public class LTTrace implements TraceAnalyzer {
    @Override
    public EVMEnvironment createEnvironmentForTrace(TraceTree trace) throws TraceException {
        return null;
    }
}

