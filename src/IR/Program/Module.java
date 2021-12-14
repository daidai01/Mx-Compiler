package IR.Program;

import IR.Operand.GlobalVarOperand;
import IR.Operand.ParaOperand;
import IR.Program.Function;
import IR.Type.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Module {
    public Map<String, GlobalVarOperand> gVarMap = new LinkedHashMap<>();
    public Map<String, GlobalVarOperand> stringMap = new LinkedHashMap<>();
    public Map<String, Function> funcMap = new LinkedHashMap<>();
    public Map<String, Function> methodMap = new LinkedHashMap<>();
    public Map<String, ClassIRType> classMap = new LinkedHashMap<>();

    //todo

    public Module() {
        VoidIRType voidIRType = new VoidIRType();
        PointerIRType stringIRType = new PointerIRType(new IntIRType(IntIRType.BitWidth.int8));
        IntIRType intIRType = new IntIRType(IntIRType.BitWidth.int32);

        //added separately
        PointerIRType mallocIntIRtype = new PointerIRType(new IntIRType(IntIRType.BitWidth.int8));
        ArrayList<ParaOperand> paras = new ArrayList<>();
        paras.add(new ParaOperand(intIRType, "size"));
        Function method = new Function(this, "malloc", mallocIntIRtype, paras);
        methodMap.put("malloc", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        method = new Function(this, "print", voidIRType, paras);
        methodMap.put("print", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        method = new Function(this, "println", voidIRType, paras);
        methodMap.put("println", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(intIRType, "n"));
        method = new Function(this, "printInt", voidIRType, paras);
        methodMap.put("printInt", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(intIRType, "n"));
        method = new Function(this, "printlnInt", voidIRType, paras);
        methodMap.put("printlnInt", method);

        paras = new ArrayList<>();
        method = new Function(this, "getString", stringIRType, paras);
        methodMap.put("getString", method);

        paras = new ArrayList<>();
        method = new Function(this, "getInt", intIRType, paras);
        methodMap.put("getInt", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(intIRType, "i"));
        method = new Function(this, "toString", stringIRType, paras);
        methodMap.put("toString", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        method = new Function(this, "_string_length", intIRType, paras);
        methodMap.put("_string_length", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        paras.add(new ParaOperand(intIRType,"left"));
        paras.add(new ParaOperand(intIRType,"right"));
        method = new Function(this, "_string_substring", stringIRType, paras);
        methodMap.put("_string_substring", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        method = new Function(this, "_string_parseInt", intIRType, paras);
        methodMap.put("_string_parseInt", method);

        paras = new ArrayList<>();
        paras.add(new ParaOperand(stringIRType, "str"));
        paras.add(new ParaOperand(intIRType,"pos"));
        method = new Function(this, "_string_ord", intIRType, paras);
        methodMap.put("_string_ord", method);
    }
}
