package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Type.BaseIRType;

public class ReturnInst extends BaseInst{
    public BaseIRType type;
    public BaseOperand operand;

    public ReturnInst(Block block,BaseIRType type,BaseOperand operand) {
        super(block);
        this.type=type;
        this.operand=operand;
    }
}
