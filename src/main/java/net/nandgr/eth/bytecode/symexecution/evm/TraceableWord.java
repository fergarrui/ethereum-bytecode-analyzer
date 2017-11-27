package net.nandgr.eth.bytecode.symexecution.evm;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.exceptions.EVMException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.Arrays;

public class TraceableWord implements Traceable {

    public static final int WORD_SIZE = 32;
    private byte[] bytes = new byte[WORD_SIZE];
    private final TraceTree trace;

    public TraceableWord(byte[] bytes, TraceTree trace) throws EVMException {
        this.trace = trace;
        if(bytes.length > 32) {
            throw new EVMException("Trying to create a word bigger than 32 bytes");
        }
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getBytes32() {
        byte[] b = new byte[WORD_SIZE];
        int lengthDif = b.length - bytes.length;

        for (int i = 0 ; i < bytes.length ; i++) {
            b[i+lengthDif] = bytes[i];
        }
        return b;
    }


    public TraceTree getTrace() {
        return trace;
    }

    public int getIntData() {
        return new BigInteger(bytes).intValue();
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "{" +
             "0x" + Hex.encodeHexString(bytes) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceableWord that = (TraceableWord) o;
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
