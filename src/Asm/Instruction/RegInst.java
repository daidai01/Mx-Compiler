package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class RegInst extends BaseInst {
    public enum AluOp {
        add, sub, slt, xor, or, and, sll, srl, sra, mul, div, rem
    }

    public AluOp op;
    public Register src1;
    public Register src2;

    public RegInst(AsmBlock block, Register register, AluOp op, Register src1, Register src2) {
        super(block, register);
        this.op = op;
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public HashSet<Register> getUses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(src1);
        uses.add(src2);
        return uses;
    }

    @Override
    public HashSet<Register> getDefs() {
        HashSet<Register> defs = new HashSet<>();
        defs.add(register);
        return defs;
    }

    @Override
    public String toString() {
        return op + " " + register + ", " + src1 + ", " + src2;
    }
}
