package net.nandgr.eth.bytecode.symexecution.trace.revert;

import net.nandgr.eth.exceptions.TraceException;
import org.apache.commons.codec.binary.Hex;
import java.math.BigInteger;

public class AndRevert implements OperationRevert {

    @Override
    public BigInteger revert(BigInteger value, BigInteger constantInt) throws TraceException {
        // TODO FIX
        return value;
//        if(constantInt.not().equals(BigInteger.ZERO)) {
//            return value;
//        }
//        String message = "Cannot revert AND for value=" + Hex.encodeHexString(value.toByteArray()) + " and constant=" + Hex.encodeHexString(constantInt.toByteArray());
//        throw new TraceException(message);
    }
}
