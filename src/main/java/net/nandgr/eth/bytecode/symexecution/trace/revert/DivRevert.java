package net.nandgr.eth.bytecode.symexecution.trace.revert;

import net.nandgr.eth.exceptions.TraceException;

import java.math.BigInteger;

public class DivRevert implements OperationRevert {

    @Override
    public BigInteger revert(BigInteger value, BigInteger constantInt) throws TraceException {
        return value.multiply(constantInt);
    }
}
