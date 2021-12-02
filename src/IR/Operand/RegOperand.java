package IR.Operand;

import IR.Type.BaseIRType;

public class RegOperand extends BaseOperand {
    public String name;
    //todo what else?

    public RegOperand(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
