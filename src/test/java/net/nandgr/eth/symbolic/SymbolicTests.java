package net.nandgr.eth.symbolic;

import net.nandgr.eth.AbstractSymbolicTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SymbolicTests extends AbstractSymbolicTest {

    @Test
    public void test_simple_decision_1() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(0x5, Opcodes.PUSH1, BigInteger.valueOf(0x00)),
                new Opcode(0x7, Opcodes.CALLDATALOAD, null),
                new Opcode(0x0, Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(0x2, Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(0x4, Opcodes.MUL, null),
                new Opcode(0x8, Opcodes.DIV, null),
                new Opcode(0x9, Opcodes.PUSH4, BigInteger.valueOf(0x3f7a0270)),
                new Opcode(0xe, Opcodes.EQ, null),
                new Opcode(0xf, Opcodes.PUSH1, BigInteger.valueOf(0x99)),
                new Opcode(0x11, Opcodes.JUMPI, null)
        );

        BytecodeChunk chunk2 = createChunk(1,
                new Opcode(0x99, Opcodes.PUSH1, BigInteger.valueOf(0x100)),
                new Opcode(0xa, Opcodes.STOP, null)
        );

        BytecodeChunk chunk3 = createChunk(2,
                new Opcode(0x12, Opcodes.PUSH1, BigInteger.valueOf(0x00)),
                new Opcode(0xd, Opcodes.STOP, null)
        );

        SymbolicPathsHandler symbolicPathsHandler = symPathFinder(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
            put(0x99, chunk2);
            put(0x12, chunk3);
        }});

        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();

        Map<EVMEnvironment, EVMState> executionsStates = symbolicPathsHandler.getExecutionsStates();
        Assert.assertEquals(2, executionsStates.size());

    }
}
