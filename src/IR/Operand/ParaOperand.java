package IR.Operand;

import IR.Type.BaseIRType;

public class ParaOperand extends BaseOperand {
    public String name;
    //todo what else?

    public ParaOperand(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
