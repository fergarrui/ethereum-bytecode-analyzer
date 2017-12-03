package net.nandgr.eth.bytecode.symexecution.trace.revert;

import net.nandgr.eth.exceptions.TraceException;

import java.math.BigInteger;

public interface OperationRevert {
    BigInteger revert(BigInteger value, BigInteger constantInt) throws TraceException;
}
