package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class SzInst extends BaseInst {
    public enum SzOp {
        seq, sne, sle, sge, slt, sgt
    }

    public SzOp op;
    public Register src;

    public SzInst(AsmBlock block, Register register, SzOp op, Register src) {
        super(block, register);
        this.op = op;
        this.src = src;
    }

    @Override
    public HashSet<Register> uses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(src);
        return uses;
    }

    @Override
    public String toString() {
        return op + "z " + register + ", " + src;
    }
}
