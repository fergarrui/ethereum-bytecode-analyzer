package net.nandgr.eth.opcodes;

import net.nandgr.eth.AbstractSymbolicTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Sha3OpcodeTest extends AbstractSymbolicTest {

    @Test
    public void test_sha3() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.MSTORE, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x1)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x20)),
                new Opcode(Opcodes.MSTORE, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.SHA3, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        Map<Integer, TraceableWord> memory = evmState.getMemory().getMemory();
        assertTrue(stack.size() == 1);

        TraceableWord memoryContents1 = memory.get(0x0);
        assertEquals(0x5, memoryContents1.getIntData());

        TraceableWord memoryContents2 = memory.get(0x20);
        assertEquals(0x1, memoryContents2.getIntData());

        TraceableWord hashedWord = stack.pop();
        assertEquals("e2689cd4a84e23ad2f564004f1c9013e9589d260bde6380aba3ca7e09e4df40c", Hex.encodeHexString(hashedWord.getBytes()));
    }
}
