package IR.Operand;

import IR.Program.Function;
import IR.Type.BaseIRType;

public class ParaOperand extends BaseOperand {
    public String name;
    public Function func;

    public ParaOperand(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public boolean isConst() {
        return false;
    }
}
