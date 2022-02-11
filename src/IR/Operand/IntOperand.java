package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;
import IR.Type.IntIRType;

import java.util.HashSet;

public class IntOperand extends BaseOperand {
    public int value;

    public IntOperand(int value, int size) {
        super(new IntIRType(size));
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
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
