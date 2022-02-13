package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class BrInst extends BaseInst {
    public enum BrOp {
        beq, bne, blt, bge
    }

    public BrOp op;
    public Register src1;
    public Register src2;
    public AsmBlock targetBlock;

    public BrInst(AsmBlock block, BrOp op, Register src1, Register src2, AsmBlock targetBlock) {
        super(block, null);
        this.op = op;
        this.src1 = src1;
        this.src2 = src2;
        this.targetBlock = targetBlock;
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
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return op + " " + src1 + ", " + src2 + ", " + targetBlock;
    }
}
