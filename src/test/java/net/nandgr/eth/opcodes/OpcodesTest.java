package net.nandgr.eth.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment.EVMEnvironmentBuilder;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import net.nandgr.eth.exceptions.EVMException;
import org.junit.Test;
import java.math.BigInteger;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpcodesTest extends AbstractOpcodesTest {

    @Test
    public void test_calldatasize() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.CALLDATASIZE, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a0270000000000000000000000000000000000000000000000000000000000000002c", 16);
        byte[] callDataBytes = callData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0x24, stack.pop().getIntData());
    }

    @Test
    public void test_calldaload_exact_size() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a027000099900000000000000000000000000000000000000000000000099", 16);
        BigInteger expectedCallData = new BigInteger("3f7a027000099900000000000000000000000000000000000000000000000099", 16);

        byte[] callDataBytes = callData.toByteArray();
        byte[] expectedCallDataBytes = expectedCallData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallDataBytes, stack.pop().getBytes()));
    }

    @Test
    public void test_calldataload_long_size() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a0270000000000000000000000000000000000000000000000000000000000000002c", 16);
        BigInteger expectedCallData = new BigInteger("3f7a027000000000000000000000000000000000000000000000000000000000", 16);
        byte[] callDataBytes = callData.toByteArray();
        byte[] expectedCallDataBytes = expectedCallData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallDataBytes, stack.pop().getBytes()));
    }

    @Test
    public void test_calldaload_short_size() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a0270000999", 16);
        BigInteger expectedCallData = new BigInteger("3f7a027000099900000000000000000000000000000000000000000000000000", 16);

        byte[] callDataBytes = callData.toByteArray();
        byte[] expectedCallDataBytes = expectedCallData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallDataBytes, stack.pop().getBytes()));
    }

    @Test
    public void test_calldaload_non_zero_index() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a0270000999", 16);
        BigInteger expectedCallData = new BigInteger("0999000000000000000000000000000000000000000000000000000000000000", 16);

        byte[] callDataBytes = callData.toByteArray();
        byte[] expectedCallDataBytes = expectedCallData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallDataBytes, stack.pop().getBytes()));
    }

    @Test(expected = EVMException.class)
    public void test_calldaload_too_big_index() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("3f7a01", 16);
        BigInteger expectedCallData = new BigInteger("0999000000000000000000000000000000000000000000000000000000000000", 16);

        byte[] callDataBytes = callData.toByteArray();
        byte[] expectedCallDataBytes = expectedCallData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallDataBytes, stack.pop().getBytes()));
    }

    @Test
    public void test_calldaload_second_parameter() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x4)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callData = new BigInteger("20bec2320000000000000000000000000000000000000000000000000000000000000005000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000035945530000000000000000000000000000000000000000000000000000000000", 16);
        BigInteger expectedCallData = BigInteger.valueOf(0x5);

        byte[] callDataBytes = callData.toByteArray();
        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallData(callDataBytes).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        TraceableWord value = stack.pop();
        assertTrue(expectedCallData.intValue() == value.getIntData());
    }

    @Test
    public void test_callvalue() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.CALLVALUE, null),
                new Opcode(Opcodes.STOP, null)
        );
        BigInteger callValue = BigInteger.valueOf(0x200);
        BigInteger expectedCallValue = BigInteger.valueOf(0x200);;

        EVMEnvironment inputs = new EVMEnvironmentBuilder().setCallValue(callValue).build();
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }}, inputs);

        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertTrue(Arrays.equals(expectedCallValue.toByteArray(), stack.pop().getBytes()));
    }
}
