package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Operand.IntOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;
import IR.Type.BaseIRType;

import java.util.HashSet;

public class GetElePtrInst extends BaseInst {
    public BaseIRType type;
    public BaseOperand pointer;
    public BaseOperand arrayOffset;
    public IntOperand eleOffset;

    public GetElePtrInst(IRBlock block, Register register, BaseIRType type, BaseOperand pointer, BaseOperand arrayOffset, IntOperand eleOffset) {
        super(block, register);
        this.type = type;
        this.pointer = pointer;
        this.arrayOffset = arrayOffset;
        this.eleOffset = eleOffset;
        pointer.addUse(this);
        arrayOffset.addUse(this);
        register.defInst = this;
    }

    @Override
    public String toString() {
        String eleOffsetStr = eleOffset == null ? "" : ", " + eleOffset.type.toString() + " " + eleOffset.toString();
        return register.toString() + " = geteleptr inbounds " + type.toString() + ", "
                + pointer.type.toString() + " " + pointer.toString() + ", "
                + arrayOffset.type.toString() + " " + arrayOffset.toString()
                + eleOffsetStr;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.add(pointer);
        uses.add(arrayOffset);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        pointer.removeUse(this);
        arrayOffset.removeUse(this);
        if (eleOffset != null) eleOffset.removeUse(this);
    }
}
