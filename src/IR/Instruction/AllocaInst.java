package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Program.IRBlock;
import IR.Operand.Register;
import IR.Type.PointerIRType;

import java.util.HashSet;

public class AllocaInst extends BaseInst {
    public AllocaInst(IRBlock block, Register register) {
        super(block, register);
        register.defInst = this;
    }

    @Override
    public String toString() {
        return register.toString() + " = " + "alloca " + ((PointerIRType) register.type).basicType.toString() + ", align " + ((PointerIRType) register.type).basicType.size() / 8;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        return new HashSet<>();
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
    }
}
