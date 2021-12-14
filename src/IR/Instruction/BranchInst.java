package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;

public class BranchInst extends BaseInst {
    public BaseOperand condition;
    public Block trueBranch;
    public Block falseBranch;

    public BranchInst(Block block,BaseOperand condition,Block trueBranch, Block falseBranch) {
        super(block);
        this.condition=condition;
        this.trueBranch=trueBranch;
        this.falseBranch=falseBranch;
    }
}
