package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class ImmInst extends BaseInst {
    public enum ImmOp {
        addi, subi, slti, xori, ori, andi, slli, srli, srai, muli, divi, remi
    }

    public ImmOp op;
    public Register src;
    public Imm imm;

    public ImmInst(AsmBlock block, Register register, ImmOp op, Register src, Imm imm) {
        super(block, register);
        this.op = op;
        this.src = src;
        this.imm = imm;
    }

    @Override
    public HashSet<Register> uses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(src);
        return uses;
    }

    @Override
    public String toString() {
        return op + " " + register + ", " + src + ", " + imm.value;
    }
}
