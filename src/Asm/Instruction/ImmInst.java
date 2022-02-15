package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class ImmInst extends BaseInst {

    public RegInst.AluOp op;
    public Register src;
    public Imm imm;

    public ImmInst(AsmBlock block, Register register, RegInst.AluOp op, Register src, Imm imm) {
        super(block, register);
        this.op = op;
        this.src = src;
        this.imm = imm;
    }

    @Override
    public HashSet<Register> getUses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(src);
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
        if (src == replaced) replaced = replacer;
    }

    @Override
    public String toString() {
        return op + "i " + register + ", " + src + ", " + imm.value;
    }
}
