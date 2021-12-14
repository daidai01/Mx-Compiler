package IR.Program;

import IR.Operand.ParaOperand;
import IR.Operand.Register;
import IR.Type.BaseIRType;
import IR.Type.VoidIRType;

import java.util.ArrayList;
import java.util.LinkedList;

public class Function {
    public Module module = null;
    public String name = null;
    public ArrayList<ParaOperand> paras = null;
    public BaseIRType returnType = new VoidIRType();
    public Register returnValue = null;

    public Block beginBlock = null;
    public Block endBlock = null;
    public Block returnBlock = null;
    public LinkedList<Block> blocks = new LinkedList<Block>();

    Function(Module module, String name, BaseIRType returnType, ArrayList<ParaOperand> paras) {
        this.module = module;
        this.name = name;
        this.returnType = returnType;
        this.paras = paras;
    }


}
