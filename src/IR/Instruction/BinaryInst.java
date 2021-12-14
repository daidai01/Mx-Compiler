package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

public class BinaryInst extends BaseInst{
    public String operator;
    public BaseOperand leftOp;
    public BaseOperand rightOp;
    public Register register;

    public BinaryInst(Block block, String operator, BaseOperand leftOp, BaseOperand rightOp, Register register) {
        super(block);
        this.operator=operator;
        this.leftOp=leftOp;
        this.rightOp=rightOp;
        this.register=register;
    }
}
