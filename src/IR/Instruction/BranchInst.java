package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;

import java.util.HashSet;

public class BranchInst extends BaseInst {
    public BaseOperand condition;
    public IRBlock trueBranch;
    public IRBlock falseBranch;

    public BranchInst(IRBlock block, BaseOperand condition, IRBlock trueBranch, IRBlock falseBranch) {
        super(block, null);
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        condition.addUse(this);
    }

    @Override
    public String toString() {
        return "br " + condition.type.toString() + " " + condition.toString() + ", label %" + trueBranch.name + ", label %" + falseBranch.name;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(condition);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeTerminalInst();
        condition.removeUse(this);
    }

    @Override
    public void replaceUse(BaseOperand replaced, BaseOperand replacer) {
        if (condition == replaced) condition = replacer;
    }
}
