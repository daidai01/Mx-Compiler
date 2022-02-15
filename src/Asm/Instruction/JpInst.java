package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class JpInst extends BaseInst {
    public AsmBlock targetBlock;

    public JpInst(AsmBlock block, AsmBlock targetBlock) {
        super(block, null);
        this.targetBlock = targetBlock;
    }

    @Override
    public HashSet<Register> getUses() {
        return new HashSet<>();
    }

    @Override
    public HashSet<Register> getDefs() {
        return new HashSet<>();
    }

    @Override
    public void replaceUse(Register replaced, Register replacer) {}

    @Override
    public String toString() {
        return "j " + targetBlock;
    }
}
