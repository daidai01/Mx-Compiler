package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.HashSet;

public class BitCastInst extends BaseInst {
    public BaseOperand operand;

    public BitCastInst(IRBlock block, Register register, BaseOperand operand) {
        super(block, register);
        this.operand = operand;
        register.defInst = this;
        operand.addUse(this);
    }

    @Override
    public String toString() {
        return register.toString() + " = bitcast " + operand.type.toString() + " " + operand.toString() + " to " + register.type.toString();
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(operand);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        operand.removeUse(this);
    }
}
