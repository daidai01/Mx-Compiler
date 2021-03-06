package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.BaseIRType;

import java.util.HashSet;

public class Register extends BaseOperand {
    public String name;
    public BaseInst defInst;
    public HashSet<BaseInst> uses = new HashSet<>();

    public Register(BaseIRType type, String name) {
        super(type);
        this.name = name;
    }

    public void replaceUse(BaseOperand operand) {
        for (BaseInst inst : uses) {
            inst.replaceUse(this, operand);
            operand.addUse(inst);
        }
    }

    @Override
    public String toString() {
        return "%" + name;
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
