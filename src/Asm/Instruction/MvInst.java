package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class MvInst extends BaseInst {
    public Register origin;

    public MvInst(AsmBlock block, Register register, Register origin) {
        super(block, register);
        this.origin = origin;
    }

    @Override
    public HashSet<Register> uses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(origin);
        return uses;
    }

    @Override
    public String toString() {
        return "mv " + register + ", " + origin;
    }
}
