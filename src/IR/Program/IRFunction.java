package IR.Program;

import IR.Operand.ParaOperand;
import IR.Operand.Register;
import IR.Type.BaseIRType;
import IR.Type.VoidIRType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class IRFunction {
    public String name;
    public ArrayList<ParaOperand> paras = new ArrayList<>();
    public BaseIRType returnType = new VoidIRType();
    public IRBlock entryBlock = new IRBlock("entry");
    public IRBlock exitBlock = null;
    public IRBlock returnBlock = null;
    public LinkedHashSet<IRBlock> blocks = new LinkedHashSet<>();
    public HashSet<Register> vars = new HashSet<>();
    public HashSet<IRFunction> callFuncs = new HashSet<>();
    public ParaOperand classPtr = null;

    public IRFunction(String name) {
        this.name = name;
        blocks.add(entryBlock);
    }

    public IRFunction(String name, BaseIRType returnType) {
        this.name = name;
        this.returnType = returnType;
        blocks.add(entryBlock);
    }

    public void setClassPtr(ParaOperand classPtr) {
        this.classPtr = classPtr;
        paras.add(classPtr);
    }
}
