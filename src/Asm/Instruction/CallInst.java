package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;
import Asm.Program.AsmFunction;
import Asm.Program.AsmRoot;

import java.util.HashSet;

public class CallInst extends BaseInst {
    public AsmFunction func;
    public AsmRoot asmRoot;

    public CallInst(AsmBlock block, AsmFunction func, AsmRoot asmRoot) {
        super(block, null);
        this.func = func;
        this.asmRoot = asmRoot;
    }

    @Override
    public HashSet<Register> getUses() {
        HashSet<Register> uses = new HashSet<>();
        uses.addAll(func.paras);
        return uses;
    }

    @Override
    public HashSet<Register> getDefs() {
        return new HashSet<>(asmRoot.callerRegs);
    }

    @Override
    public void replaceUse(Register replaced, Register replacer) {}

    @Override
    public String toString() {
        return "call " + func.name;
    }
}
