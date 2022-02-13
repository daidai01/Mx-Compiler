package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;

import java.util.HashSet;

public class ReturnInst extends BaseInst {
    public BaseOperand value;

    public ReturnInst(IRBlock block, BaseOperand value) {
        super(block, null);
        this.value = value;
        if (value != null) value.addUse(this);
    }

    @Override
    public String toString() {
        if (value == null) return "ret void";
        else return "ret " + value.type.toString() + " " + value.toString();
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        if (value != null) uses.add(value);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeTerminalInst();
        if (value != null) value.removeUse(this);
    }

    @Override
    public void replaceUse(BaseOperand replaced, BaseOperand replacer) {
        if (value == replaced) value = replacer;
    }
}
