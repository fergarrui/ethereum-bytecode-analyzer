package net.nandgr.eth.symbolic;

import net.nandgr.eth.AbstractSymbolicTest;
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

import static org.junit.Assert.*;

public class ExecutionTraceTest extends AbstractSymbolicTest {

    @Test
    public void test_static_trace_1() throws Exception {
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

    @Test
    public void test_symbolic_trace1() throws Exception {
        /*
            Creates a trace like:
                        EQ
                       /  \
                      /    \
                   MUL     ADD
                   /\       /\
                  /  \     /  \
                0x5  0x2  0x3 CALLDATALOAD
         */
        BytecodeChunk chunk = createChunk(0,
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x5)),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x2)),
                new Opcode(Opcodes.MUL, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x0)),
                new Opcode(Opcodes.CALLDATALOAD, null),
                new Opcode(Opcodes.PUSH1, BigInteger.valueOf(0x3)),
                new Opcode(Opcodes.ADD, null),
                new Opcode(Opcodes.EQ, null),
                new Opcode(Opcodes.STOP, null)
        );

        EVMState evmState = symExecute(new HashMap<Integer, BytecodeChunk>() {{
            put(0, chunk);
        }});
        EVMStack stack = evmState.getStack();
        TraceableWord word = stack.pop();
        TraceTree eqTrace = word.getTrace();
        List<TraceTree> eqChildren = eqTrace.getChildren();
        ExecutionTrace eqExecutionTrace = eqTrace.getExecutionTrace();
        SymbolicTransformation eqSymbolicTransformation = eqExecutionTrace.getSymbolicTransformation();

        // testing children, symbolic propagation and inputs
        assertEquals(2, eqChildren.size());
        assertEquals(Opcodes.EQ, eqExecutionTrace.getOpcode().getOpcode());
        assertTrue(eqExecutionTrace.isSymbolic());
        assertEquals(1, eqSymbolicTransformation.getNumOfInputs());
        assertEquals(Opcodes.EQ, eqSymbolicTransformation.getOperation().getOpcode());

        TraceTree eqChild0 = eqChildren.get(0);
        TraceTree eqChild1 = eqChildren.get(1);
        // testing symbolic propagation
        assertTrue(eqChild0.getExecutionTrace().isSymbolic());
        assertFalse(eqChild1.getExecutionTrace().isSymbolic());
        List<TraceableWord> child1Output = eqChild1.getExecutionTrace().getOutput();
        assertEquals(1, child1Output.size());
        // test we can get the output value if it is not symbolic
        assertEquals(0xa, child1Output.get(0).getIntData());

        List<SymbolicTransformation> eqTransformations = eqSymbolicTransformation.getTransformations();

        // testing transformations
        assertEquals(2, eqTransformations.size());

        SymbolicTransformation symbolicTransformation0 = eqTransformations.get(0);
        SymbolicTransformation symbolicTransformation1 = eqTransformations.get(1);

        assertEquals(1, symbolicTransformation0.getNumOfInputs());
        assertEquals(0, symbolicTransformation1.getNumOfInputs());

        List<SymbolicTransformation> transformations0 = symbolicTransformation0.getTransformations();
        List<SymbolicTransformation> transformations1 = symbolicTransformation1.getTransformations();

        assertEquals(2, transformations0.size());
        assertEquals(2, transformations1.size());

        SymbolicTransformation transformations00 = transformations0.get(0);
        SymbolicTransformation transformations01 = transformations0.get(1);

        assertEquals(0, transformations00.getNumOfInputs());
        assertEquals(1, transformations01.getNumOfInputs());
        assertEquals(Opcodes.ADD, symbolicTransformation0.getOperation().getOpcode());
    }
}
