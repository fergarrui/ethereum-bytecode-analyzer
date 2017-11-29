package net.nandgr.eth.symbolic;

import net.nandgr.eth.AbstractOpcodesTest;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Opcodes;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.ExecutionTrace;
import net.nandgr.eth.bytecode.symexecution.SymbolicTransformation;
import net.nandgr.eth.bytecode.symexecution.TraceTree;
import net.nandgr.eth.bytecode.symexecution.evm.EVMStack;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.bytecode.symexecution.evm.TraceableWord;
import org.junit.Test;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TODO Probably this test will be included in Opcodes tests
// TODO when those are split
public class ExecutionTraceTest extends AbstractOpcodesTest {

    @Test
    public void test_trace_1() throws Exception {
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x60)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x40)),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.STOP, null)
        );
        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree wordTrace = word.getTrace();
        List<TraceTree> children = wordTrace.getChildren();

        assertEquals(2, children.size());

        ExecutionTrace executionTrace = wordTrace.getExecutionTrace();

        assertEquals(Opcodes.ADD, executionTrace.getOpcode().getOpcode());
        assertEquals(false, executionTrace.isSymbolic());

        List<TraceableWord> inputs = executionTrace.getInput();

        assertEquals(2, inputs.size());

        TraceableWord input0 = inputs.get(0);
        TraceableWord input1 = inputs.get(1);

        assertEquals(0x40, input0.getIntData());
        assertEquals(0x60, input1.getIntData());

        SymbolicTransformation symbolicTransformation = executionTrace.getSymbolicTransformation();

        assertEquals(0, symbolicTransformation.getNumOfInputs());
        assertEquals(Opcodes.ADD, symbolicTransformation.getOperation().getOpcode());

        List<SymbolicTransformation> transformations = symbolicTransformation.getTransformations();

        assertEquals(2, transformations.size());

        List<TraceableWord> output = executionTrace.getOutput();

        assertEquals(1, output.size());

        TraceableWord output0 = output.get(0);

        assertEquals(0xA0, output0.getIntData());
        TraceTree traceTree0 = children.get(0);
        TraceTree traceTree1 = children.get(1);

        assertTrue(traceTree0.getChildren().isEmpty());
        assertTrue(traceTree1.getChildren().isEmpty());

        ExecutionTrace executionTrace0 = traceTree0.getExecutionTrace();
        ExecutionTrace executionTrace1 = traceTree1.getExecutionTrace();

        assertTrue(executionTrace0.getInput().isEmpty());
        assertTrue(executionTrace1.getInput().isEmpty());
        assertEquals(Opcodes.PUSH1, executionTrace0.getOpcode().getOpcode());
        assertEquals(Opcodes.PUSH1, executionTrace1.getOpcode().getOpcode());
        assertFalse(executionTrace0.isSymbolic());
        assertFalse(executionTrace1.isSymbolic());

        SymbolicTransformation symbolicTransformation0 = executionTrace0.getSymbolicTransformation();
        SymbolicTransformation symbolicTransformation1 = executionTrace1.getSymbolicTransformation();

        assertEquals(0, symbolicTransformation0.getNumOfInputs());
        assertEquals(0, symbolicTransformation1.getNumOfInputs());
        assertEquals(Opcodes.PUSH1, symbolicTransformation0.getOperation().getOpcode());
        assertEquals(Opcodes.PUSH1, symbolicTransformation1.getOperation().getOpcode());
        assertTrue(symbolicTransformation0.getTransformations().isEmpty());
        assertTrue(symbolicTransformation1.getTransformations().isEmpty());
    }
}
