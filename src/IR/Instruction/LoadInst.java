package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Type.BaseIRType;

public class LoadInst extends BaseInst {
    public BaseOperand pointer;
    public BaseIRType type;
    public Register register;

    public LoadInst(Block block, BaseOperand pointer, BaseIRType type, Register register) {
        super(block);
        this.pointer = pointer;
        this.type = type;
        this.register = register;
    }
}
