package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;

public class Register extends BaseOperand {
    public String name;
    public BaseInst inst;

    public Register(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
