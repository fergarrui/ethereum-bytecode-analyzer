package net.nandgr.eth.bytecode.beans;

public class ContractBytecode {

    private final BytecodeSection constructorSection;
    private final BytecodeSection functionsSection;

    public ContractBytecode(BytecodeSection constructorSection, BytecodeSection functionsSection) {
        this.constructorSection = constructorSection;
        this.functionsSection = functionsSection;
    }

    public BytecodeSection getConstructorSection() {
        return constructorSection;
    }

    public BytecodeSection getFunctionsSection() {
        return functionsSection;
    }
}
