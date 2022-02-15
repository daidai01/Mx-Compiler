package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

abstract public class BaseInst {
    public AsmBlock block;
    public Register register;
    public BaseInst preInst = null;
    public BaseInst nxtInst = null;

    public BaseInst(AsmBlock block, Register register) {
        this.block = block;
        this.register = register;
    }

    abstract public HashSet<Register> getUses();

    abstract public HashSet<Register> getDefs();

    abstract public void replaceUse(Register replaced, Register replacer);

    abstract public String toString();

    public void addPreInst(BaseInst inst) {
        inst.preInst = preInst;
        inst.nxtInst = this;
        if (preInst == null) block.headInst = inst;
        else preInst.nxtInst = inst;
        preInst = inst;
    }

    public void addNxtInst(BaseInst inst) {
        inst.nxtInst = nxtInst;
        inst.preInst = this;
        if (nxtInst == null) block.tailInst = inst;
        else nxtInst.preInst = inst;
        nxtInst = inst;
    }

    public void replaceInst(BaseInst replacer) {
        replacer.preInst = preInst;
        replacer.nxtInst = nxtInst;
        if (preInst == null) block.headInst = replacer;
        else preInst.nxtInst = replacer;
        if (nxtInst == null) block.tailInst = replacer;
        else nxtInst.preInst = replacer;
    }

    public void remove() {
        if (preInst == null) block.headInst = nxtInst;
        else preInst.nxtInst = nxtInst;
        if (nxtInst == null) block.tailInst = preInst;
        else nxtInst.preInst = preInst;
    }

    public void updateImm(int stackSize) {
        if (this instanceof ImmInst && ((ImmInst) this).imm.canReverse)
            ((ImmInst) this).imm = new Imm((((ImmInst) this).imm.reverse ? -1 : 1) * stackSize + ((ImmInst) this).imm.value);
        else if (this instanceof LdInst && ((LdInst) this).offset.canReverse)
            ((LdInst) this).offset = new Imm(stackSize + ((LdInst) this).offset.value);
        else if (this instanceof LiInst && ((LiInst) this).imm.canReverse)
            ((LiInst) this).imm = new Imm(stackSize + ((LiInst) this).imm.value);
        else if (this instanceof StInst && ((StInst) this).offset.canReverse)
            ((StInst) this).offset = new Imm(stackSize + ((StInst) this).offset.value);
    }

    public void replaceTargetBlock(AsmBlock replaced, AsmBlock replacer) {
        if (this instanceof BrInst && replaced == ((BrInst) this).targetBlock)
            ((BrInst) this).targetBlock = replacer;
        if (this instanceof BzInst && replaced == ((BzInst) this).targetBlock)
            ((BzInst) this).targetBlock = replacer;
        if (this instanceof JpInst && replaced == ((JpInst) this).targetBlock)
            ((JpInst) this).targetBlock = replacer;
    }
}
