package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Type.BaseIRType;

public class BitCastInst extends BaseInst{
    public BaseIRType type;
    public BaseOperand operand;
    public Register register;

    public BitCastInst(Block block,BaseOperand operand,Register register) {
        super(block);
        this.type=register.type;
        this.operand=operand;
        this.register=register;
    }
}
