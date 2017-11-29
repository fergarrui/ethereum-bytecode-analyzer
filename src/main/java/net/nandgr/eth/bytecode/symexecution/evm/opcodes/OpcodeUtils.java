package net.nandgr.eth.bytecode.symexecution.evm.opcodes;

import net.nandgr.eth.Opcodes;
import net.nandgr.eth.utils.Lists;
import java.util.List;

public class OpcodeUtils {

    private static final List<Opcodes> symbolicOpcodes = Lists.of(
            Opcodes.GAS,
            Opcodes.ADDRESS,
            Opcodes.BALANCE,
            Opcodes.ORIGIN,
            Opcodes.CALLER,
            Opcodes.CALLVALUE,
            Opcodes.CALLDATALOAD,
            Opcodes.CALLDATASIZE,
            Opcodes.CALLDATACOPY,
            Opcodes.CODESIZE,
            Opcodes.CODECOPY,
            Opcodes.GASPRICE,
            Opcodes.EXTCODESIZE,
            Opcodes.EXTCODECOPY,
            Opcodes.BLOCKHASH,
            Opcodes.COINBASE,
            Opcodes.TIMESTAMP,
            Opcodes.NUMBER,
            Opcodes.DIFFICULTY,
            Opcodes.GASLIMIT
    );

    public static boolean isSymbolic(Opcodes opcode) {
        return symbolicOpcodes.contains(opcode);
    }
}
