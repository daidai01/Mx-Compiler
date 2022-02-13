package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.HashSet;

public class MoveInst extends BaseInst {
    public BaseOperand src;

    public MoveInst(IRBlock block, Register register, BaseOperand src, boolean addUse) {
        super(block, register);
        this.src = src;
        if (addUse) src.addUse(this);
    }

    @Override
    public String toString() {
        return "mv " + src.type.toString() + " " + register.toString() + " " + src.toString();
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(src);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        src.removeUse(this);
    }

    @Override
    public void replaceUse(BaseOperand replaced, BaseOperand replacer) {
        if (src == replaced) src = replacer;
    }
}
