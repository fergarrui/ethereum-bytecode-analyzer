package net.nandgr.eth.bytecode.symexecution.trace.revert;

import java.math.BigInteger;

public class AddRevert implements OperationRevert {

    @Override
    public BigInteger revert(BigInteger value, BigInteger constantInt) {
        return value.subtract(constantInt);
    }
}
