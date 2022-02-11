package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;
import IR.Type.BoolIRType;

import java.util.HashSet;

public class ZextInst extends BaseInst {
    public BaseOperand src;

    public ZextInst(IRBlock block, Register register, BaseOperand src) {
        super(block, register);
        this.src = src;
        register.defInst = this;
        src.addUse(this);
    }

    @Override
    public String toString() {
        String key = "zext";
        int src_size = src.type instanceof BoolIRType ? 1 : src.type.size();
        int target_size = register.type instanceof BoolIRType ? 1 : register.type.size();
        if (src_size > target_size) key = "trunc";
        return register.toString() + " = " + key + " " + src.type.toString() + " " + src.toString() + " to " + register.type.toString();
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(src);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        src.removeUse(this);
    }
}
