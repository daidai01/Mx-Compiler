package IR.Operand;

import IR.Type.BaseIRType;
import IR.Type.BoolIRType;

public class BoolOperand extends BaseOperand {
    boolean value;

    public BoolOperand(boolean value) {
        super(new BoolIRType());
        this.value = value;
    }

    @Override
    public boolean isConst() {
        return true;
    }
}
