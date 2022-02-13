package IR.Instruction;

import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.HashSet;

public class LoadInst extends BaseInst {
    public BaseOperand address;

    public LoadInst(IRBlock block, Register register, BaseOperand address) {
        super(block, register);
        this.address = address;
        register.defInst = this;
        address.addUse(this);
    }

    @Override
    public String toString() {
        return "%" + register.name + " = load " + register.type.toString() + ", " + address.type.toString() + " " + address.toString() + ", align " + register.type.size() / 8;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(address);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        address.removeUse(this);
    }

    @Override
    public void replaceUse(BaseOperand replaced, BaseOperand replacer) {
        if (address == replaced) address = replacer;
    }
}
