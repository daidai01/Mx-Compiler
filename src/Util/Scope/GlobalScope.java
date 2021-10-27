package Util.Scope;

import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Entity.VarEntity;
import Util.Error.SemanticError;
import Util.Position;
import Util.Type.BaseType;
import Util.Type.PrimitiveType;

import java.util.HashMap;

public class GlobalScope extends BaseScope {
    public HashMap<String, ClassEntity> classes = new HashMap<>();

    public GlobalScope() {
        super(null);
        //primitive types
        PrimitiveType boolType = new PrimitiveType("bool");
        PrimitiveType intType = new PrimitiveType("int");
        PrimitiveType voidType = new PrimitiveType("void");
        PrimitiveType stringType = new PrimitiveType("string");
        PrimitiveType nullType = new PrimitiveType("null");

        //builtin types
        ClassEntity boolEntity = new ClassEntity(boolType, "bool", new ClassScope(this));
        classes.put("bool", boolEntity);
        ClassEntity intEntity = new ClassEntity(intType, "int", new ClassScope(this));
        classes.put("int", intEntity);
        ClassEntity voidEntity = new ClassEntity(voidType, "void", new ClassScope(this));
        classes.put("void", voidEntity);
        ClassEntity stringEntity = new ClassEntity(stringType, "string", new ClassScope(this));
        classes.put("string", stringEntity);
        ClassEntity nullEntity = new ClassEntity(nullType, "null", new ClassScope(this));
        classes.put("null", nullEntity);

        FuncScope funcScope = new FuncScope(this);
        Position funcPos = new Position(0, 0);
        FuncEntity funcEntity;

        //string methods
        funcEntity = new FuncEntity(intType, "length", funcScope);
        stringEntity.defineFunc("length", funcEntity, funcPos);

        funcEntity = new FuncEntity(stringType, "substring", funcScope);
        funcEntity.addPara(new VarEntity(intType, "left", false), funcPos);
        funcEntity.addPara(new VarEntity(intType, "right", false), funcPos);
        stringEntity.defineFunc("substring", funcEntity, funcPos);

        funcEntity = new FuncEntity(intType, "parseInt", funcScope);
        stringEntity.defineFunc("parseInt", funcEntity, funcPos);

        funcEntity = new FuncEntity(intType, "ord", funcScope);
        funcEntity.addPara(new VarEntity(intType, "pos", false), funcPos);
        stringEntity.defineFunc("ord", funcEntity, funcPos);

        //builtin functions
        funcEntity = new FuncEntity(voidType, "print", funcScope);
        funcEntity.addPara(new VarEntity(stringType, "str", false), funcPos);
        defineFunc("print", funcEntity, funcPos);

        funcEntity = new FuncEntity(voidType, "println", funcScope);
        funcEntity.addPara(new VarEntity(stringType, "str", false), funcPos);
        defineFunc("println", funcEntity, funcPos);

        funcEntity = new FuncEntity(voidType, "printInt", funcScope);
        funcEntity.addPara(new VarEntity(intType, "n", false), funcPos);
        defineFunc("printInt", funcEntity, funcPos);

        funcEntity = new FuncEntity(voidType, "printlnInt", funcScope);
        funcEntity.addPara(new VarEntity(intType, "n", false), funcPos);
        defineFunc("printlnInt", funcEntity, funcPos);

        funcEntity = new FuncEntity(stringType, "getString", funcScope);
        defineFunc("getString", funcEntity, funcPos);

        funcEntity = new FuncEntity(intType, "getInt", funcScope);
        defineFunc("getInt", funcEntity, funcPos);

        funcEntity = new FuncEntity(stringType, "toString", funcScope);
        funcEntity.addPara(new VarEntity(intType, "i", false), funcPos);
        defineFunc("toString", funcEntity, funcPos);

        funcEntity = new FuncEntity(intType, "size", funcScope);
        defineFunc("size", funcEntity, funcPos);
    }

    public void defineClass(String name, ClassEntity classEntity, Position pos) {
        if (classes.containsKey(name)) throw new SemanticError(name + ": redefine class ", pos);
        if (containVar(name, false)) throw new SemanticError(name + ": class name conflicts with a variable", pos);
        classes.put(name, classEntity);
    }

    public boolean containClass(String name) {
        return classes.containsKey(name);
    }

    public ClassEntity getClass(String name, Position pos) {
        if (classes.containsKey(name)) return classes.get(name);
        throw new SemanticError(name + ": undefined class", pos);
    }
}
