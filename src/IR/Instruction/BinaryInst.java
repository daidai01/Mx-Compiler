package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.HashSet;

public class BinaryInst extends BaseInst {
    public String op; //   * / % << >> & | ^ - +
    public BaseOperand leftOp;
    public BaseOperand rightOp;
    public boolean commutable;

    public BinaryInst(IRBlock block, Register register, String op, BaseOperand leftOp, BaseOperand rightOp) {
        super(block, register);
        this.op = op;
        this.leftOp = leftOp;
        this.rightOp = rightOp;
        register.defInst = this;
        leftOp.addUse(this);
        rightOp.addUse(this);
        if (op.equals("+") || op.equals("*") || op.equals("&") || op.equals("|") || op.equals("^")) commutable = true;
        else commutable = false;
    }

    @Override
    public String toString() {
        return register.toString() + " = " + op + " " + leftOp.type.toString() + " " + leftOp.toString() + ", " + rightOp.toString();
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
