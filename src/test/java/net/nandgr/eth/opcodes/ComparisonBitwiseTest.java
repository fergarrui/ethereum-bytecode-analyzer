package net.nandgr.eth.opcodes;

import net.nandgr.eth.AbstractSymbolicTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComparisonBitwiseTest extends AbstractSymbolicTest {

    @Test
    public void test_LT_is_lower_than() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.LT, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(1, stack.pop().getIntData());
    }

    @Test
    public void test_LT_is_higher_than() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.LT, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0, stack.pop().getIntData());
    }

    @Test
    public void test_EQ_is_equal_than() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(1, stack.pop().getIntData());
    }

    @Test
    public void test_LT_is_not_equal_than() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0, stack.pop().getIntData());
    }

    @Test
    public void test_IsZero_is_zero() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0)),
                new Opcode(Opcodes.ISZERO, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(1, stack.pop().getIntData());
    }

    @Test
    public void test_IsZero_is_not_zero() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.ISZERO, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0, stack.pop().getIntData());
    }

    @Test
    public void test_And() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xffffff)),
                new Opcode(Opcodes.AND, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0x404040, stack.pop().getIntData());
    }

    @Test
    public void test_And_2() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xff0000)),
                new Opcode(Opcodes.AND, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0x400000, stack.pop().getIntData());
    }

    @Test
    public void test_Or() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xffffff)),
                new Opcode(Opcodes.OR, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0xffffff, stack.pop().getIntData());
    }

    @Test
    public void test_Or_2() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xff0000)),
                new Opcode(Opcodes.OR, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0xff4040, stack.pop().getIntData());
    }

    @Test
    public void test_Xor() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xffffff)),
                new Opcode(Opcodes.XOR, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0xbfbfbf, stack.pop().getIntData());
    }

    @Test
    public void test_Xor_2() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x404040)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0xff0000)),
                new Opcode(Opcodes.XOR, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0xbf4040, stack.pop().getIntData());
    }

}
