package net.nandgr.eth.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArithmeticOpcodesTest extends AbstractOpcodesTest {

    @Test
    public void test_add() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(100, stack.pop().getIntData());
    }

    @Test
    public void test_add_chain() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(20)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(120, stack.pop().getIntData());
    }

    @Test
    public void test_sub() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.SUB, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(20, stack.pop().getIntData());
    }

    @Test
    public void test_sub_chain() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(20)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.SUB, null),
                new Opcode(Opcodes.SUB, null),
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
    public void test_div() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(20, stack.pop().getIntData());
    }

    @Test
    public void test_div_2() throws Exception {

        BigInteger div = new BigInteger("3f7a027000000000000000000000000000000000000000000000000000000000", 16);
        BigInteger divBy = new BigInteger("100000000000000000000000000000000000000000000000000000000", 16);
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, divBy),
                new Opcode(Opcodes.PUSH1, div),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        TraceableWord result = stack.pop();
        assertTrue(Arrays.equals(Hex.decodeHex("3f7a0270"), result.getBytes()));
    }

    @Test
    public void test_div_chain() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(4)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(5, stack.pop().getIntData());
    }

    @Test
    public void test_mul() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(4)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.MUL, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(8, stack.pop().getIntData());
    }
}
