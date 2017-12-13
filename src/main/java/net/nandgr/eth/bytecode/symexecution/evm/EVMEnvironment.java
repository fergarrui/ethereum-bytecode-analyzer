package net.nandgr.eth.bytecode.symexecution.evm;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EVMEnvironment {

    private final List<Byte> callData;
    private final BigInteger callValue;

    private EVMEnvironment(List<Byte> callData, BigInteger callValue) {
        this.callData = callData;
        this.callValue = callValue;
    }

    public List<Byte> getCallData() {
        return callData;
    }

    public BigInteger getCallValue() {
        return callValue;
    }

    @Override
    public String toString() {
        return "EVMEnvironment{" +
                "callData=" + callData +
                ", callValue=" + callValue +
                '}';
    }

    public String getCallDataHex() {
        byte[] bytes = ArrayUtils.toPrimitive(callData.toArray(new Byte[callData.size()]));
        return Hex.encodeHexString(bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EVMEnvironment that = (EVMEnvironment) o;
        if (callData != null ? !callData.equals(that.callData) : that.callData != null) return false;
        return callValue != null ? callValue.equals(that.callValue) : that.callValue == null;
    }

    @Override
    public int hashCode() {
        int result = callData != null ? callData.hashCode() : 0;
        result = 31 * result + (callValue != null ? callValue.hashCode() : 0);
        return result;
    }

    public static class EVMEnvironmentBuilder {

        private List<Byte> callData = new ArrayList<>();
        private BigInteger callValue = BigInteger.valueOf(0);

        public EVMEnvironmentBuilder setCallData(byte[] callData) {
            Byte[] bytes = new Byte[callData.length];
            Arrays.setAll(bytes, n -> callData[n]);
            this.callData = Arrays.asList(bytes);
            return this;
        }

        public EVMEnvironmentBuilder setCallValue(BigInteger value) {
            this.callValue = value;
            return this;
        }

        public EVMEnvironment build() {
            return new EVMEnvironment(callData, callValue);
        }
    }
}
