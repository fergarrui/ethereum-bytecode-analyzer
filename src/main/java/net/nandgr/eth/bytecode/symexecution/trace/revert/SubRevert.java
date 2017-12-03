package net.nandgr.eth.bytecode.symexecution.trace.revert;

import java.math.BigInteger;

public class SubRevert implements OperationRevert {

    @Override
    public BigInteger revert(BigInteger value, BigInteger constantInt) {
        return value.add(constantInt);
    }
}
