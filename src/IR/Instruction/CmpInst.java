package IR.Instruction;

import IR.Operand.NullOperand;
import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.HashSet;

public class CmpInst extends BaseInst {
    public String op;
    public BaseOperand leftOp;
    public BaseOperand rightOp;

    public CmpInst(IRBlock block, Register register, String op, BaseOperand leftOp, BaseOperand rightOp) {
        super(block, register);
        this.op = op;
        this.leftOp = leftOp;
        this.rightOp = rightOp;
        register.defInst = this;
        leftOp.addUse(this);
        rightOp.addUse(this);
    }

    @Override
    public String toString() {
        String typeStr;
        if (leftOp instanceof NullOperand && rightOp instanceof NullOperand) typeStr = "int*";
        else if (leftOp instanceof NullOperand) typeStr = rightOp.type.toString();
        else typeStr = leftOp.type.toString();
        return register.toString() + " = icmp " + op + " " + typeStr + " " + leftOp.toString() + ", " + rightOp.toString();
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(leftOp);
        uses.add(rightOp);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        leftOp.removeUse(this);
        rightOp.removeUse(this);
    }

    @Override
    public void replaceUse(BaseOperand replaced, BaseOperand replacer) {
        if (leftOp == replaced) leftOp = replacer;
        if (rightOp == replaced) rightOp = replacer;
    }
}
