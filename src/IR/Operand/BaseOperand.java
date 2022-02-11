package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;

import java.util.HashSet;

abstract public class BaseOperand {
    public BaseIRType type;

    public BaseOperand(BaseIRType type) {
        this.type = type;
    }

    abstract public String toString();

    abstract public HashSet<BaseInst> getUses();

    abstract public void addUse(BaseInst inst);

    abstract public void removeUse(BaseInst inst);

    public String getName() {
        if (this instanceof ParaOperand) return ((ParaOperand) this).name;
        else if (this instanceof GlobalVarOperand) return ((GlobalVarOperand) this).name;
        else if (this instanceof Register) return ((Register) this).name;
        else return "const";
    }
}
