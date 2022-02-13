package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class BzInst extends BaseInst {
    public enum BzOp {
        beq, bne, ble, bge, blt, bgt
    }

    public BzOp op;
    public AsmBlock targetBlock;
    public Register cond;

    public BzInst(AsmBlock block, BzOp op, AsmBlock targetBlock, Register cond) {
        super(block, null);
        this.op = op;
        this.targetBlock = targetBlock;
        this.cond = cond;
    }

    @Override
    public HashSet<Register> getUses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(cond);
        return uses;
    }

    @Override
    public HashSet<Register> getDefs() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return op + "z " + cond + ", " + targetBlock;
    }
}
