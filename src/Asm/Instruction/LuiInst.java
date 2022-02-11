package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class LuiInst extends BaseInst {
    public Imm address;

    public LuiInst(AsmBlock block, Register register, Imm address) {
        super(block, register);
        this.address = address;
    }

    @Override
    public HashSet<Register> uses() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return "lui " + register + ", " + address;
    }
}
