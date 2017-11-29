package net.nandgr.eth.bytecode.symexecution.trace;

import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.TraceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class EQTrace implements TraceAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(EQTrace.class);

    public EVMEnvironment buildEnvironment(EVMEnvironment environment, TraceableWord confitionWord) throws TraceException {
        TraceTree trace = confitionWord.getTrace();
        List<TraceTree> children = trace.getChildren();
        if (children.size() != 2) {
            String errorMessage = "Trace must have 2 children and have: " + children.size();
            logger.error(errorMessage);
            throw new TraceException(errorMessage);
        }
        TraceTree children0 = children.get(0);
        TraceTree children1 = children.get(1);
        // TODO -
        return null;
    }
}
