package IR.Operand;

import IR.Type.BaseIRType;

abstract public class BaseOperand {
    BaseIRType type;

    public BaseOperand(BaseIRType type) {
        this.type = type;
    }

    abstract public boolean isConst();
}
