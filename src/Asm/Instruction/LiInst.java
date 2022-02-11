package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class LiInst extends BaseInst {
    public Imm imm;

    public LiInst(AsmBlock block, Register register, Imm imm) {
        super(block, register);
        this.imm = imm;
    }

    @Override
    public HashSet<Register> uses() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return "li " + register + ", " + imm;
    }
}
