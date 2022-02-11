package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;
import IR.Type.BoolIRType;

import java.util.HashSet;

public class BoolOperand extends BaseOperand {
    public boolean value;

    public BoolOperand(boolean value) {
        super(new BoolIRType());
        this.value = value;
    }

    public int getIntValue() {
        return value ? 1 : 0;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public HashSet<BaseInst> getUses() {
        return new HashSet<>();
    }

    @Override
    public void addUse(BaseInst inst) {

    }

    @Override
    public void removeUse(BaseInst inst) {

    }
}
