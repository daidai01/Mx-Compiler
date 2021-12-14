package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;

public class StoreInst extends BaseInst {
    public BaseOperand pointer;
    public BaseOperand value;

    public StoreInst(Block block,BaseOperand pointer,BaseOperand value) {
        super(block);
        this.pointer=pointer;
        this.value=value;
    }
}
