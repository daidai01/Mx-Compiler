package Util.Scope;

import AST.Program.TypeNode;
import Util.Entity.BaseEntity;
import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Entity.VarEntity;
import Util.Error.SemanticError;
import Util.Position;
import Util.Type.ArrayType;
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
        PrimitiveType nullType = new PrimitiveType("null");
        ClassEntity stringType = new ClassEntity("string");

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

//        FuncScope funcScope = new FuncScope(this);
//        Position builtinFuncPos = new Position(0, 0);
        FuncEntity funcEntity;

        //string methods
        funcEntity = new FuncEntity(intType, "length", new FuncScope(this), true);
        stringEntity.defineFunc("length", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(stringType, "substring", new FuncScope(this), true);
        funcEntity.addPara(new VarEntity(intType, "left", false), new Position(0, 0));
        funcEntity.addPara(new VarEntity(intType, "right", false), new Position(0, 0));
        stringEntity.defineFunc("substring", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(intType, "parseInt", new FuncScope(this), true);
        stringEntity.defineFunc("parseInt", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(intType, "ord", new FuncScope(this), true);
        funcEntity.addPara(new VarEntity(intType, "pos", false), new Position(0, 0));
        stringEntity.defineFunc("ord", funcEntity, new Position(0, 0));

        //builtin functions
        funcEntity = new FuncEntity(voidType, "print", new FuncScope(this));
        funcEntity.addPara(new VarEntity(stringType, "str", false), new Position(0, 0));
        defineFunc("print", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(voidType, "println", new FuncScope(this));
        funcEntity.addPara(new VarEntity(stringType, "str", false), new Position(0, 0));
        defineFunc("println", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(voidType, "printInt", new FuncScope(this));
        funcEntity.addPara(new VarEntity(intType, "n", false), new Position(0, 0));
        defineFunc("printInt", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(voidType, "printlnInt", new FuncScope(this));
        funcEntity.addPara(new VarEntity(intType, "n", false), new Position(0, 0));
        defineFunc("printlnInt", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(stringType, "getString", new FuncScope(this));
        defineFunc("getString", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(intType, "getInt", new FuncScope(this));
        defineFunc("getInt", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(stringType, "toString", new FuncScope(this));
        funcEntity.addPara(new VarEntity(intType, "i", false), new Position(0, 0));
        defineFunc("toString", funcEntity, new Position(0, 0));

        funcEntity = new FuncEntity(intType, "size", new FuncScope(this));
        defineFunc("size", funcEntity, new Position(0, 0));
    }

    public void defineClass(String name, ClassEntity classEntity, Position pos) {
        if (classes.containsKey(name))
            throw new SemanticError(name + ": redefine class ", pos);
        if (containVar(name, false))
            throw new SemanticError(name + ": class name conflicts with a variable", pos);
        classes.put(name, classEntity);
    }

    public boolean containClass(String name) {
        return classes.containsKey(name);
    }

    public ClassEntity getClass(String name, Position pos) {
        if (!classes.containsKey(name))
            throw new SemanticError(name + ": undefined class", pos);
        else return classes.get(name);
    }

    public ClassEntity getClass(TypeNode typeNode) {
        if (typeNode.dim == 0)
            return getClass(typeNode.name, typeNode.pos);
        else {
            ClassScope classScope = getClass(typeNode.name, typeNode.pos).scope;
            return new ClassEntity(typeNode.baseType, typeNode.name, classScope);
        }
    }

    public BaseType getBaseType(TypeNode typeNode) {
        if (typeNode == null) return null;
        else if (typeNode.dim == 0) return getClass(typeNode.name, typeNode.pos).type;
        else return new ArrayType(getClass(typeNode.name, typeNode.pos).name, typeNode.dim);
    }
}
