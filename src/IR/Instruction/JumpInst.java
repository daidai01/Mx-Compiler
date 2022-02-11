package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Program.IRBlock;

import java.util.HashSet;

public class JumpInst extends BaseInst {
    public IRBlock targetBlock;

    public JumpInst(IRBlock block, IRBlock targetBlock) {
        super(block, null);
        this.targetBlock = targetBlock;
    }

    @Override
    public String toString() {
        return "br label %" + targetBlock.name;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        return new HashSet<>();
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeTerminalInst();
    }
}
