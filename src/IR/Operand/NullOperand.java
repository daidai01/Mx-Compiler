package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;
import IR.Type.PointerIRType;
import IR.Type.VoidIRType;

import java.util.HashSet;

public class NullOperand extends BaseOperand {
    public NullOperand() {
        super(new PointerIRType(new VoidIRType(), false));
    }

    @Override
    public String toString() {
        return "null";
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
