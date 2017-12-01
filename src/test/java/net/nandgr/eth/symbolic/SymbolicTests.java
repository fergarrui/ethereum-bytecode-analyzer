package net.nandgr.eth.symbolic;

import net.nandgr.eth.AbstractOpcodesTest;
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

public class SymbolicTests extends AbstractOpcodesTest {

    @Test
    public void test_simple_decision() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.CALLVALUE, null),
                new Opcode(Opcodes.ISZERO, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x999)),
                new Opcode(Opcodes.JUMPI, null),
                new Opcode(Opcodes.STOP, null)
        );
        SymbolicPathsHandler symbolicPathsHandler = symPathFinder(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();
    }

    @Test
    public void test_simple_decision_2() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x4)),
                new Opcode(Opcodes.CALLDATASIZE, null),
                new Opcode(Opcodes.LT, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x999)),
                new Opcode(Opcodes.JUMPI, null),
                new Opcode(Opcodes.STOP, null)
        );
        SymbolicPathsHandler symbolicPathsHandler = symPathFinder(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();
    }

    @Test
    public void test_simple_decision_3() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH29, new BigInteger("100000000000000000000000000000000000000000000000000000000", 16)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x00)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.DIV, null),
                new Opcode(Opcodes.PUSH4, BigInteger.valueOf(0x3f7a0270)),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x99)),
                new Opcode(Opcodes.JUMPI, null),
                new Opcode(Opcodes.STOP, null)
        );
        SymbolicPathsHandler symbolicPathsHandler = symPathFinder(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        symbolicPathsHandler.startSymbolicExecution();
        symbolicPathsHandler.await();
    }

    @Test
    public void test_simple_decision_4() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(0x0, Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(0x2, Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(0x4, Opcodes.MUL, null),
                new Opcode(0x5, Opcodes.PUSH1, BigInteger.valueOf(0x00)),
                new Opcode(0x7, Opcodes.CALLDATALOAD, null),
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

        EVMEnvironment evmEnvironment = new EVMEnvironment.EVMEnvironmentBuilder()
                .setCallData(Hex.decodeHex("000000000000000000000000000000000000000000000000000000027AC41860"))
                .build();

        symbolicPathsHandler.startSymbolicExecution(evmEnvironment);
        symbolicPathsHandler.await();
        EVMState evmState = symbolicPathsHandler.getExecutionsStates().get(evmEnvironment);

        TraceableWord pop = evmState.getStack().pop();
        int intData = pop.getIntData();
        System.out.println(intData);
        Assert.assertTrue(intData == 0x100);
    }
}
