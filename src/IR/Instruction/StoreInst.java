package IR.Instruction;

import IR.Operand.NullOperand;
import IR.Program.IRBlock;
import IR.Operand.BaseOperand;
import IR.Type.PointerIRType;

import java.util.HashSet;

public class StoreInst extends BaseInst {
    public BaseOperand address;
    public BaseOperand value;

    public StoreInst(IRBlock block, BaseOperand address, BaseOperand value) {
        super(block, null);
        this.address = address;
        this.value = value;
        address.addUse(this);
        value.addUse(this);
    }

    @Override
    public String toString() {
        String str = value.type.toString();
        if (value instanceof NullOperand)
            str = ((PointerIRType) address.type).basicType.toString();
        return "store " + str + " " + value.toString() + ", " + address.type.toString() + " " + address.toString() + ", align " + value.type.size() / 8;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(value);
        uses.add(address);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        address.removeUse(this);
        value.removeUse(this);
    }
}
