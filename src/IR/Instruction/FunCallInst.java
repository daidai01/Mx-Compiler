package IR.Instruction;

import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Type.BoolIRType;

import java.util.ArrayList;
import java.util.HashSet;

public class FunCallInst extends BaseInst {
    public IRFunction func;
    public ArrayList<BaseOperand> paras;

    public FunCallInst(IRBlock block, Register register, IRFunction func, ArrayList<BaseOperand> paras) {
        super(block, register);
        this.func = func;
        this.paras = paras;
        if (register != null) register.defInst = this;
        paras.forEach(para -> para.addUse(this));
    }

    @Override
    public String toString() {
        String str;
        if (register == null) str = "call void";
        else str = register.toString() + " = call " + register.type.toString() + " ";
        str += "@" + func.name + "(";
        for (int i = 0; i < paras.size(); ++i) {
            if (i > 0) str += ", ";
            if (paras.get(i).type instanceof BoolIRType) str += "i8";
            else str += paras.get(i).type.toString();
            str += " " + paras.get(i).toString();
        }
        str += ")";
        return str;
    }

    @Override
    public HashSet<BaseOperand> getUses() {
        HashSet<BaseOperand> uses = new HashSet<>();
        uses.addAll(paras);
        return uses;
    }

    @Override
    public void remove(boolean fromBlock) {
        if (fromBlock) block.removeInst(this);
        paras.forEach(para -> para.removeUse(this));
    }
}
