package IR.Instruction;

import IR.Program.Block;
import IR.Program.Function;
import IR.Operand.BaseOperand;
import IR.Operand.Register;

import java.util.ArrayList;

public class FunCallInst extends BaseInst{
    public Function func;
    public ArrayList<BaseOperand> paras;
    public Register register;

    public FunCallInst(Block block,Function func, ArrayList<BaseOperand> paras,Register register) {
        super(block);
        this.func=func;
        this.paras=paras;
        this.register=register;
    }
}
