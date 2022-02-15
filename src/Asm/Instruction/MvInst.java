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
    public HashSet<Register> getUses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(origin);
        return uses;
    }

    @Override
    public HashSet<Register> getDefs() {
        HashSet<Register> defs = new HashSet<>();
        defs.add(register);
        return defs;
    }

    @Override
    public void replaceUse(Register replaced, Register replacer) {
        if (origin == replaced) origin = replacer;
    }

    @Override
    public String toString() {
        return "mv " + register + ", " + origin;
    }
}
