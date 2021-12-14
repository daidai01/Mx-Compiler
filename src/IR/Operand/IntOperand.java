package IR.Operand;

import IR.Type.BaseIRType;
import IR.Type.IntIRType;

public class IntOperand extends BaseOperand {
    public int value;

    public IntOperand(int value, IntIRType.BitWidth bitWidth) {
        super(new IntIRType(bitWidth));
        this.value = value;
    }

    @Override
    public boolean isConst() {
        return true;
    }
}
