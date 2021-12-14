package IR.Instruction;

import IR.Program.Block;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.ArrayList;

public class PhiInst extends BaseInst {
    public ArrayList<Block> blocks;
    public ArrayList<BaseOperand> operands;
    public Register register;

    public PhiInst(Block block, ArrayList<Block> blocks, ArrayList<BaseOperand> operands, Register register) {
        super(block);
        this.blocks = blocks;
        this.operands = operands;
        this.register = register;
    }
}
