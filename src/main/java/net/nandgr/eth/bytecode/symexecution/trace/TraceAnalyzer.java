package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.exceptions.TraceException;

public interface TraceAnalyzer {
    EVMEnvironment createEnvironmentForTrace(TraceTree trace, EVMEnvironment environment) throws TraceException;
}
