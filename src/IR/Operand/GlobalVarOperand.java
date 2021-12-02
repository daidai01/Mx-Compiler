package IR.Operand;

import IR.Type.BaseIRType;

public class GlobalVarOperand extends BaseOperand {
    public String name;
    public BaseOperand init;

    public GlobalVarOperand(BaseIRType type, String name, BaseOperand init) {
        super(type);
        this.name = name;
        this.init = init;
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
