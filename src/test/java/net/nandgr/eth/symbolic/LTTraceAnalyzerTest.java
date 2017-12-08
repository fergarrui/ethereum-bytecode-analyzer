package net.nandgr.eth.symbolic;

import net.nandgr.eth.AbstractSymbolicTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.bytecode.symexecution.trace.comparisons.LTTraceAnalyzer;
import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;

public class LTTraceAnalyzerTest extends AbstractSymbolicTest {

    @Test
    public void test_lt() throws Exception {

        BytecodeChunk chunk = createChunk(0,
                new Opcode(0x0,Opcodes.PUSH1, BigInteger.valueOf(0x4)),
                new Opcode(0x2, Opcodes.CALLDATASIZE, null),
                new Opcode(0x3,Opcodes.LT, null),
                new Opcode(0x7, Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});

        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree trace = word.getTrace();

        LTTraceAnalyzer eqTraceAnalyzer = new LTTraceAnalyzer();
        EVMEnvironment environmentForTrace = eqTraceAnalyzer.createEnvironmentForTrace(trace, createDefaultEnvironment());
        Assert.assertEquals(3, environmentForTrace.getCallData().size());
    }
}
