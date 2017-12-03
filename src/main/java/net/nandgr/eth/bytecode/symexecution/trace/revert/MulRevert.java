package net.nandgr.eth.bytecode.symexecution.trace.revert;

import net.nandgr.eth.exceptions.TraceException;

import java.math.BigInteger;

public class MulRevert implements OperationRevert {

    @Override
    public BigInteger revert(BigInteger value, BigInteger constantInt) throws TraceException {
        if (constantInt.compareTo(BigInteger.valueOf(0)) == 0) {
            throw new TraceException("Cannot revert MUL if the constant is 0");
        }
        return value.divide(constantInt);
    }
}
