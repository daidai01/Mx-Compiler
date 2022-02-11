package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class LaInst extends BaseInst {
    public Register origin;

    public LaInst(AsmBlock block, Register register, Register origin) {
        super(block, register);
        this.origin = origin;
    }

    @Override
    public HashSet<Register> uses() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return "la " + register + ", " + origin;
    }
}
