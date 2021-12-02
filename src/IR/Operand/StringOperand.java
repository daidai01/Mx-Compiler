package IR.Operand;

import IR.Type.ArrayIRType;
import IR.Type.BaseIRType;
import IR.Type.IntIRType;
import IR.Type.PointerIRType;

public class StringOperand extends BaseOperand {
    public String value;

    public StringOperand(String value) {
        super(new PointerIRType(new ArrayIRType(new IntIRType(1), value.length())));
        this.value = value;
    }

    @Override
    public boolean isConst() {
        return true;
    }
}
