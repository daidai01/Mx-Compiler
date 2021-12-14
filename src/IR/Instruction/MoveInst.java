package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

public class MoveInst extends BaseInst{
    public BaseOperand src;
    public Register register;

    public MoveInst(Block block,BaseOperand src,Register register) {
        super(block);
        this.src=src;
        this.register=register;
    }
}
