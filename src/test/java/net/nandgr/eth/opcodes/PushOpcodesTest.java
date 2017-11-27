package net.nandgr.eth.opcodes;

import net.nandgr.eth.AbstractOpcodesTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PushOpcodesTest extends AbstractOpcodesTest {

    @Test
    public void test_push1() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(30)),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue( stack.size() == 2);
        assertEquals(30, stack.pop().getIntData());
        assertEquals(60, stack.pop().getIntData());
    }

    @Test
    public void test_push_32() throws Exception {
        BigInteger parameter1 = new BigInteger("33fd08d0d466e732a0cda838127ac0cb6f8a7c0db7dc8eb7f140a31111811192", 16);
        BigInteger parameter2 = new BigInteger("100000000000000000000000000000000000000000000000000000000", 16);
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH32, parameter1),
                new Opcode(Opcodes.PUSH29, parameter2),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue( stack.size() == 2);
        byte[] bytes = stack.pop().getBytes();
        byte[] expectedBytes1 = parameter1.toByteArray();
        byte[] expectedBytes2 = parameter2.toByteArray();
        assertTrue(Arrays.equals(expectedBytes2, bytes));

        bytes = stack.pop().getBytes();
        assertTrue(Arrays.equals(expectedBytes1, bytes));
    }
}
