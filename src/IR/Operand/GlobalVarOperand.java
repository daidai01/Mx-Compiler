package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;

import java.util.HashSet;

public class GlobalVarOperand extends BaseOperand {
    public String name;
    public HashSet<BaseInst> uses = new HashSet<>();

    public GlobalVarOperand(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public HashSet<BaseInst> getUses() {
        return uses;
    }

    @Override
    public void addUse(BaseInst inst) {
        uses.add(inst);
    }

    @Override
    public void removeUse(BaseInst inst) {
        uses.remove(inst);
    }
}
