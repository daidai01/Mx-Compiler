package IR.Instruction;

import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;

import java.util.HashSet;

abstract public class BaseInst {
    public IRBlock block;
    public BaseInst preInst = null;
    public BaseInst nxtInst = null;
    public Register register;

    public BaseInst(IRBlock block, Register register) {
        this.block = block;
        this.register = register;
    }

    public void removeInst() {
        if (nxtInst == null) block.tailInst = preInst;
        else nxtInst.preInst = preInst;
        if (preInst == null) block.headInst = nxtInst;
        else preInst.nxtInst = nxtInst;
    }

    abstract public String toString();

    abstract public HashSet<BaseOperand> getUses();

    abstract public void remove(boolean fromBlock);
}
