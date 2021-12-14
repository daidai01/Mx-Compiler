package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Type.BaseIRType;

public class CmpInst extends BaseInst {
    public String op;
    public BaseOperand leftOp;
    public BaseOperand rightOp;
    public BaseIRType type;
    public Register register;

    public CmpInst(Block block, String op, BaseOperand leftOp, BaseOperand rightOp, BaseIRType type, Register register) {
        super(block);
        this.op = op;
        this.leftOp = leftOp;
        this.rightOp = rightOp;
        this.type = type;
        this.register = register;
    }
}
