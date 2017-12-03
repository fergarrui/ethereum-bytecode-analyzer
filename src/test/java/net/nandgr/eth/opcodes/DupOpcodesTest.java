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

public class DupOpcodesTest extends AbstractSymbolicTest {

    @Test
    public void test_dup_1() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.DUP1, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 2);
        assertEquals(40, stack.pop().getIntData());
        assertEquals(40, stack.pop().getIntData());
    }

    @Test
    public void test_dup_5_with_10_elements_in_stack() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(1)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(3)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(4)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(5)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(6)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(7)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(8)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(9)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(10)),
                new Opcode(Opcodes.DUP5, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 12);
        assertEquals(6, stack.pop().getIntData());
        assertEquals(10, stack.pop().getIntData());
        assertEquals(9, stack.pop().getIntData());
        assertEquals(8, stack.pop().getIntData());
        assertEquals(7, stack.pop().getIntData());
        assertEquals(6, stack.pop().getIntData());
        assertEquals(5, stack.pop().getIntData());
        assertEquals(4, stack.pop().getIntData());
        assertEquals(3, stack.pop().getIntData());
        assertEquals(2, stack.pop().getIntData());
        assertEquals(1, stack.pop().getIntData());
        assertEquals(0, stack.pop().getIntData());
    }

    @Test
    public void test_dup_10() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(1)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(2)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(3)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(4)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(5)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(6)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(7)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(8)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(9)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(10)),
                new Opcode(Opcodes.DUP10, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 12);
        assertEquals(1, stack.pop().getIntData());
        assertEquals(10, stack.pop().getIntData());
        assertEquals(9, stack.pop().getIntData());
        assertEquals(8, stack.pop().getIntData());
        assertEquals(7, stack.pop().getIntData());
        assertEquals(6, stack.pop().getIntData());
        assertEquals(5, stack.pop().getIntData());
        assertEquals(4, stack.pop().getIntData());
        assertEquals(3, stack.pop().getIntData());
        assertEquals(2, stack.pop().getIntData());
        assertEquals(1, stack.pop().getIntData());
        assertEquals(0, stack.pop().getIntData());
    }
}
