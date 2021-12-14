package IR.Instruction;

import IR.Program.Block;
import IR.Operand.Register;
import IR.Type.BaseIRType;

public class AllocaInst extends BaseInst{
    public BaseIRType type;
    public Register register;

    public AllocaInst(Block block, BaseIRType type, Register register) {
        super(block);
        this.type=type;
        this.register=register;
    }
}
