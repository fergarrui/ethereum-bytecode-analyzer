package net.nandgr.eth.bytecode.symexecution.evm;

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
                '}';
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
