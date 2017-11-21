package net.nandgr.eth.opcodes;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStorage;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StackMemoryStorageAndFlowOperationsTest extends AbstractOpcodesTest {

    @Test
    public void test_jump() throws Exception {
        BytecodeChunk chunk1 = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(Opcodes.JUMP, null)
        );

        BytecodeChunk chunk2 = createChunk(1,
                new Opcode(Opcodes.POP, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(100)),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk1);
            put(40, chunk2);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(100, stack.pop().getIntData());
        assertEquals(40, evmState.getPc());
    }

    @Test
    public void test_jump_i_true() throws Exception {
        BytecodeChunk chunk1 = createChunk(0,
                new Opcode(0x0, Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(0x2,Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(0x4,Opcodes.LT, null),
                new Opcode(0x5,Opcodes.PUSH1, BigInteger.valueOf(0xb)),
                new Opcode(0x7, Opcodes.JUMPI, null)
        );

        BytecodeChunk chunk2 = createChunk(1,
                new Opcode(0x9, Opcodes.PUSH1, BigInteger.valueOf(100)),
                new Opcode(0xa, Opcodes.STOP, null)
        );

        BytecodeChunk chunk3 = createChunk(2,
                new Opcode(0xc, Opcodes.PUSH1, BigInteger.valueOf(0)),
                new Opcode(0xd, Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0x0, chunk1);
            put(0x8, chunk2);
            put(0xb, chunk3);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0, stack.pop().getIntData());
        assertEquals(0xb, evmState.getPc());
    }

    @Test
    public void test_jump_i_false() throws Exception {
        BytecodeChunk chunk1 = createChunk(0,
                new Opcode(0x0, Opcodes.PUSH1, BigInteger.valueOf(40)),
                new Opcode(0x2,Opcodes.PUSH1, BigInteger.valueOf(60)),
                new Opcode(0x4,Opcodes.LT, null),
                new Opcode(0x5,Opcodes.PUSH1, BigInteger.valueOf(0xb)),
                new Opcode(0x7, Opcodes.JUMPI, null)
        );

        BytecodeChunk chunk2 = createChunk(1,
                new Opcode(0x9, Opcodes.PUSH1, BigInteger.valueOf(100)),
                new Opcode(0xa, Opcodes.STOP, null)
        );

        BytecodeChunk chunk3 = createChunk(2,
                new Opcode(0xc, Opcodes.PUSH1, BigInteger.valueOf(0)),
                new Opcode(0xd, Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0x0, chunk1);
            put(0x8, chunk2);
            put(0xb, chunk3);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(100, stack.pop().getIntData());
        assertEquals(0x8, evmState.getPc());
    }

    @Test
    public void test_pop() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.POP, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        assertEquals(0x60, stack.pop().getIntData());
    }

    @Test
    public void test_mstore() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.MSTORE, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        Map<Integer, TraceableWord> memory = evmState.getMemory().getMemory();

        assertTrue(stack.size() == 0);
        TraceableWord memoryContents = memory.get(0x40);
        assertEquals(0x60, memoryContents.getIntData());
    }

    @Test
    public void test_mload() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.MSTORE, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.MLOAD, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        Map<Integer, TraceableWord> memory = evmState.getMemory().getMemory();
        assertTrue(stack.size() == 1);
        TraceableWord memoryContents = memory.get(0x40);
        assertEquals(0x60, memoryContents.getIntData());
        assertEquals(0x60, stack.pop().getIntData());
    }

    @Test
    public void test_storage() throws Exception {
        BigInteger key = BigInteger.valueOf(0x1234);
        BigInteger value = BigInteger.valueOf(0x999);
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, value),
                new Opcode(Opcodes.PUSH1, key),
                new Opcode(Opcodes.SSTORE, null),
                new Opcode(Opcodes.PUSH1, key),
                new Opcode(Opcodes.SLOAD, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        TraceableWord valueWord = stack.pop();
        assertTrue(valueWord.getIntData() == value.intValue());
    }

    @Test
    public void test_storage_2() throws Exception {
        BigInteger key = new BigInteger("23bf72df16f8335be9a3eddfb5ef1c739b12847d13a384ec83f578699d38eb89", 16);
        BigInteger value = new BigInteger("4141410000000000000000000000000000000000000000000000000000000006", 16);
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, value),
                new Opcode(Opcodes.PUSH1, key),
                new Opcode(Opcodes.SSTORE, null),
                new Opcode(Opcodes.PUSH1, key),
                new Opcode(Opcodes.SLOAD, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        assertTrue(stack.size() == 1);
        TraceableWord valueWord = stack.pop();
        assertTrue(valueWord.getIntData() == value.intValue());
    }
}
