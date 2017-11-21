package net.nandgr.eth.blockchain;

import net.nandgr.eth.Opcode;

import java.util.List;

public class Contract {

    private String address;
    private String name;
    private String code;
    private String disassembledCode;
    private List<Opcode> opcodesList;

    public Contract(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisassembledCode() {
        return disassembledCode;
    }

    public void setDisassembledCode(String disassembledCode) {
        this.disassembledCode = disassembledCode;
    }

    public List<Opcode> getOpcodesList() {
        return opcodesList;
    }

    public void setOpcodesList(List<Opcode> opcodesList) {
        this.opcodesList = opcodesList;
    }
}
