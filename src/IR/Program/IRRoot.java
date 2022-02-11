package IR.Program;

import IR.Operand.GlobalVarOperand;
import IR.Operand.ParaOperand;
import IR.Operand.StringOperand;
import IR.Type.*;
import Util.Type.ArrayType;
import Util.Type.BaseType;

import java.util.ArrayList;
import java.util.HashMap;

public class IRRoot {
    public ArrayList<GlobalVarOperand> gVars = new ArrayList<>();
    public HashMap<String, IRFunction> funcs = new HashMap<>();
    public HashMap<String, IRFunction> builtinFuncs = new HashMap<>();
    public HashMap<String, ClassIRType> types = new HashMap<>();
    public HashMap<String, StringOperand> constStrings = new HashMap<>();
    public HashMap<String, StringOperand> usualStrings = new HashMap<>();

    public IRRoot() {
        BaseIRType intType = new IntIRType(32);
        BaseIRType boolType = new BoolIRType();
        BaseIRType stringType = new PointerIRType(new IntIRType(8), false);
        BaseIRType voidType = new VoidIRType();

        IRFunction function = new IRFunction("g_print", voidType);
        function.paras.add(new ParaOperand(stringType, "str"));
        builtinFuncs.put("print", function);

        function = new IRFunction("g_println", voidType);
        function.paras.add(new ParaOperand(stringType, "str"));
        builtinFuncs.put("println", function);

        function = new IRFunction("g_printInt", voidType);
        function.paras.add(new ParaOperand(intType, "n"));
        builtinFuncs.put("printInt", function);

        function = new IRFunction("g_printlnInt", voidType);
        function.paras.add(new ParaOperand(intType, "n"));
        builtinFuncs.put("printlnInt", function);

        function = new IRFunction("g_getString", stringType);
        builtinFuncs.put("getString", function);

        function = new IRFunction("g_getInt", intType);
        builtinFuncs.put("getInt", function);

        function = new IRFunction("g_toString", stringType);
        function.paras.add(new ParaOperand(intType, "i"));
        builtinFuncs.put("toString", function);

        function = new IRFunction("g_stringAdd", stringType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringAdd", function);

        function = new IRFunction("g_stringLess", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringLess", function);

        function = new IRFunction("g_stringLessEqual", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringLessEqual", function);

        function = new IRFunction("g_stringGreater", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringGreater", function);

        function = new IRFunction("g_stringGreaterThan", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringGreaterThan", function);

        function = new IRFunction("g_stringEqual", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringEqual", function);

        function = new IRFunction("g_stringNotEqual", boolType);
        function.paras.add(new ParaOperand(stringType, "str1"));
        function.paras.add(new ParaOperand(stringType, "str2"));
        builtinFuncs.put("stringNotEqual", function);

        function = new IRFunction("malloc", stringType);
        function.paras.add(new ParaOperand(intType, "size"));
        builtinFuncs.put("malloc", function);

        //todo __init for what
        function = new IRFunction("__init", voidType);
        function.exitBlock = function.entryBlock;
        funcs.put("__init", function);
    }

    public BaseIRType getIRType(BaseType type, boolean isMemSet) {
        if (type.isArray()) {
            BaseIRType irType = getIRType(new BaseType(((ArrayType) type).typeName), isMemSet);
            for (int i = 0; i < type.dim; ++i)
                irType = new PointerIRType(irType, false);
            return irType;
        } else if (type.isBool()) {
            if (isMemSet) return new IntIRType(8);
            else return new BoolIRType();
        } else if (type.isInt()) return new IntIRType(32);
        else if (type.isClass()) {
            if (type.typeName.equals("string")) return new PointerIRType(new IntIRType(8), false);
            else return new PointerIRType(types.get(type.typeName), false);
        } else if (type.isVoid() || type.isNull()) return new VoidIRType();
        else return new VoidIRType();
    }

    public IRFunction getInit() {
        return funcs.get("__init");
    }

    public void addConstString(String name, String value) {
        if (constStrings.containsKey(name)) return;
        StringOperand stringOperand = new StringOperand(name, value);
        constStrings.put(name, stringOperand);
        usualStrings.put(value, stringOperand);
    }
}
