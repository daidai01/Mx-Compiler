package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;

import java.util.HashSet;

public class MallocInst extends BaseInst {
    public BaseOperand size;

    public MallocInst(IRBlock block, Register register, BaseOperand size) {
        super(block, register);
        this.size = size;
        register.defInst = this;
        size.addUse(this);
    }

    @Override
    public String toString() {
        return register.toString() + " = call i8* @malloc(" + size.type.toString() + " " + size.toString() + ")";
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(size);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        size.removeUse(this);
    }
}
