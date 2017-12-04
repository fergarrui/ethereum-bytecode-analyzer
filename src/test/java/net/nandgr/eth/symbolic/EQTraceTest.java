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
import net.nandgr.eth.bytecode.symexecution.trace.EQTrace;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;

public class EQTraceTest extends AbstractSymbolicTest {

    @Test
    public void test_eq_trace_1() throws Exception {
        /*
            Creates a trace like:
                        EQ
                       /  \
                      /    \
                   MUL     ADD
                   /\       /\
                  /  \     /  \
                0x5  0x2  0x3 CALLDATALOAD
         */
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(Opcodes.MUL, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x3)),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree trace = word.getTrace();

        EQTrace eqTrace = new EQTrace();
        EVMEnvironment environmentForTrace = eqTrace.createEnvironmentForTrace(trace);
        byte[] bytes = ArrayUtils.toPrimitive((Byte[]) environmentForTrace.getCallData().toArray());
        BigInteger callDataLoad = new BigInteger(bytes);
        Assert.assertEquals(BigInteger.valueOf(0x7), callDataLoad);
    }

    @Test
    public void test_eq_trace_2() throws Exception {

        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xA)),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(1)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.SUB, null),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.MUL, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x6)),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree trace = word.getTrace();

        EQTrace eqTrace = new EQTrace();
        EVMEnvironment environmentForTrace = eqTrace.createEnvironmentForTrace(trace);
        byte[] bytes = ArrayUtils.toPrimitive((Byte[]) environmentForTrace.getCallData().toArray());
        BigInteger callDataLoad = new BigInteger(bytes);
        Assert.assertEquals(BigInteger.valueOf(0x2), callDataLoad);
    }

    @Test
    public void test_eq_trace_function_calls() throws Exception {

        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.PUSH29, new BigInteger("100000000000000000000000000000000000000000000000000000000", 16)),
                new Opcode(Opcodes.SWAP1, null),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.PUSH4, BigInteger.valueOf(0xffffffff)),
                new Opcode(Opcodes.AND, null),
                new Opcode(Opcodes.PUSH4, BigInteger.valueOf(0x3f7a0270)),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree trace = word.getTrace();

        EQTrace eqTrace = new EQTrace();
        EVMEnvironment environmentForTrace = eqTrace.createEnvironmentForTrace(trace);
        System.out.println(environmentForTrace.getCallData().size());
        byte[] bytes = ArrayUtils.toPrimitive((Byte[]) environmentForTrace.getCallData().toArray());
        BigInteger callDataLoad = new BigInteger(bytes);
        Assert.assertEquals(new BigInteger("3f7a027000000000000000000000000000000000000000000000000000000000", 16), callDataLoad);
    }
}
