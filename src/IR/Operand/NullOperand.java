package IR.Operand;

import IR.Type.BaseIRType;
import IR.Type.PointerIRType;
import IR.Type.VoidIRType;

public class NullOperand extends BaseOperand{
    public NullOperand() {
        super(new PointerIRType(new VoidIRType()));
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
