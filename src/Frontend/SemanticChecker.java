package Frontend;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.ClassDef;
import AST.Program.FuncDef;
import AST.Program.RootNode;
import AST.Program.TypeNode;
import AST.Stmt.*;
import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Entity.VarEntity;
import Util.Error.SemanticError;
import Util.Scope.BaseScope;
import Util.Scope.GlobalScope;
import Util.Type.ArrayType;
import Util.Type.BaseType;
import Util.Type.PrimitiveType;

import java.util.ArrayList;
import java.util.Stack;

//TODO: consider Lambda

public class SemanticChecker implements ASTVisitor {
    public GlobalScope globalScope = null;
    public BaseScope currentScope = null;
    public ClassEntity currentClass = null;
    public FuncEntity currentFunc = null;
    public BaseType returnType = null;
    public boolean hasReturn = false;
    public Stack<ASTNode> loops = new Stack<>();

    public SemanticChecker(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        currentScope = globalScope;
        it.defs.forEach(def -> def.accept(this));
        //main
        if (!globalScope.containFunc("main", true))
            throw new SemanticError("no main function", it.pos);
        FuncEntity mainEntity = globalScope.getFunc("main", it.pos, false);
        if (!mainEntity.type.isInt())
            throw new SemanticError("main function must return int", it.pos);
        if (!mainEntity.scope.paras.isEmpty())
            throw new SemanticError("main function should not have parameters", it.pos);
    }

    @Override
    public void visit(ClassDef it) {
        ClassEntity classEntity = globalScope.getClass(it.name, it.pos);
        currentClass = classEntity;
        currentScope = classEntity.scope;
//        classEntity.scope.vars.forEach((key, value) -> currentScope.defineVar(key, value, it.pos));
//        classEntity.scope.funcs.forEach((key, value) -> currentScope.defineFunc(key, value, it.pos));
        for (FuncDef function : it.funcDefs)
            function.accept(this);
        for (FuncDef constructor : it.constructors) {
            constructor.accept(this);
            if (!constructor.name.equals(it.name))
                throw new SemanticError("constructor's name should be the same with class", constructor.pos);
        }
        currentClass = null;
        currentScope = currentScope.parentScope;
    }

    @Override
    public void visit(FuncDef it) {
        if (it.isConstructor && !it.name.equals(currentClass.name))
            throw new SemanticError("constructor should have the same name with class", it.pos);
        returnType = new PrimitiveType("void");
        if (it.type != null) returnType = it.type.baseType;
        currentFunc = it.funcEntity;
        currentScope = currentFunc.scope;
        hasReturn = false;
        it.block.accept(this);
        currentScope = currentScope.parentScope;
        if (!returnType.isVoid() && !hasReturn && !it.name.equals("main"))
            throw new SemanticError("no return", it.pos);
        returnType = null;
        currentFunc = null;
    }

    @Override
    public void visit(TypeNode it) {
        //Empty
    }

    @Override
    public void visit(AssignExpr it) {
        it.leftSrc.accept(this);
        it.rightSrc.accept(this);
        if (!it.leftSrc.type.equals(it.rightSrc.type) && !it.rightSrc.type.isNull())
            throw new SemanticError("type mismatch", it.pos);
        if (!it.leftSrc.assignable)
            throw new SemanticError("left expression is not assignable", it.leftSrc.pos);
        it.type = it.leftSrc.type;
    }

    @Override
    public void visit(BinaryExpr it) {
        it.src1.accept(this);
        it.src2.accept(this);
        switch (it.op) {
            case "*":
            case "/":
            case "%":
            case "-":
            case "<<":
            case ">>":
            case "&":
            case "^":
            case "|":
                if (!it.src1.type.isInt() || !it.src2.type.isInt())
                    throw new SemanticError("there can only be int values", it.pos);
                it.type = new PrimitiveType("int");
                break;
            case "+":
                if (it.src1.type.isInt() && it.src2.type.isInt())
                    it.type = new PrimitiveType("int");
                else if (it.src1.type.isString() && it.src2.type.isString())
                    it.type = new PrimitiveType("string");
                else throw new SemanticError("there can only be pairs of int and string values", it.pos);
                break;
            case ">":
            case "<":
            case ">=":
            case "<=":
                if (it.src1.type.isInt() && it.src2.type.isInt()) ;
                else if (it.src1.type.isString() && it.src2.type.isString()) ;
                else throw new SemanticError("there can only be pairs of int and string values", it.pos);
                it.type = new PrimitiveType("bool");
                break;
            case "==":
            case "!=":
                if (!it.src1.type.equals(it.src2.type))
                    throw new SemanticError("type mismatch", it.pos);
                else it.type = new PrimitiveType("bool");
                break;
            case "&&":
            case "||":
                if (!it.src1.type.isBool() || !it.src2.type.isBool())
                    throw new SemanticError("there can only be bool values", it.pos);
                else it.type = new PrimitiveType("bool");
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(BoolLiteralExpr it) {
        it.type = new PrimitiveType("bool");
    }

    @Override
    public void visit(ExprList it) {
    }

    @Override
    public void visit(FuncCallExpr it) {
        it.name.accept(this);
        //TODO check this
        if (!(it.name.type instanceof FuncEntity))
            throw new SemanticError("not a function", it.pos);
        FuncEntity funcEntity = (FuncEntity) it.name.type;
        ArrayList<BaseExpr> givenParas = it.paras;
        ArrayList<VarEntity> requiredParas = funcEntity.scope.paras;
        if (givenParas.size() != requiredParas.size())
            throw new SemanticError("wrong number of parameters", it.pos);
        for (int i = 0; i < givenParas.size(); ++i) {
            givenParas.forEach(para -> para.accept(this));
            if (!givenParas.get(i).type.equals(requiredParas.get(i).type))
                throw new SemanticError("type mismatch", givenParas.get(i).pos);
        }
        it.type = it.name.type;
    }

    @Override
    public void visit(FuncExpr it) {
        it.type = currentScope.getFunc(it.name, it.pos, true);
    }

    @Override
    public void visit(IndexExpr it) {
        it.index.accept(this);
        if (!it.index.type.isInt())
            throw new SemanticError("index is not int", it.pos);
        it.identifier.accept(this);
        if (!it.identifier.type.isArray())
            throw new SemanticError("not an array", it.pos);
        if (it.identifier.type.dim > 1)
            it.type = new ArrayType(it.identifier.type.typeName, it.identifier.type.dim - 1);
        else it.type = new PrimitiveType(it.identifier.type.typeName);
    }

    @Override
    public void visit(IntLiteralExpr it) {
        it.type = new PrimitiveType("int");
    }

    @Override
    public void visit(LambdaExpr it) {
        //TODO
    }

    @Override
    public void visit(MemberExpr it) {
        it.expr.accept(this);
        //TODO check this
        if (it.isFunc) {
            if (it.expr.type.isArray()) {
                if (!it.identifier.equals("size"))
                    throw new SemanticError("array doesn't have this function", it.pos);
                it.type = globalScope.getFunc("size", it.pos, false);
                return;
            }
            if (!it.expr.type.isClass())
                throw new SemanticError("not a class", it.expr.pos);
            ClassEntity classEntity = (ClassEntity) it.expr.type;
            if (classEntity.scope.containFunc(it.identifier, false))
                it.type = classEntity.scope.getFunc(it.identifier, it.pos, false);
            else throw new SemanticError("no sush function", it.pos);
//            String[] builtinFunc = {"length", "substring", "parseInt", "ord", "print", "println",
//                    "printInt", "printlnInt", "getString", "getInt", "toString", "size"};
//            if (classEntity.scope != null && classEntity.scope.containFunc(it.identifier, false))
//                it.type = classEntity.scope.getFunc(it.identifier, it.pos, false).type;
//            else {
//                boolean isBuiltin = false;
//                for (String func : builtinFunc) {
//                    if (it.identifier.equals(func))
//                        isBuiltin = true;
//                }
//                if (isBuiltin)
//                    it.type = globalScope.getFunc(it.identifier, it.pos, false).type;
//                else throw new SemanticError("no such method", it.pos);
//            }
        } else {
            if (!it.expr.type.isClass())
                throw new SemanticError("not a class", it.expr.pos);
            ClassEntity classEntity;
//            if (it.expr.type instanceof ClassEntity)
            classEntity = (ClassEntity) it.expr.type;
//            else classEntity = new ClassEntity(it.expr.type, it.expr.type.typeName, null);
            if (classEntity.scope.containVar(it.identifier, false))
                it.type = classEntity.scope.getVar(it.identifier, it.pos, false).type;
            else throw new SemanticError("no such member", it.pos);
        }
    }

    @Override
    public void visit(NewExpr it) {
        for (BaseExpr expr : it.exprs) {
            expr.accept(this);
            if (!expr.type.isInt())
                throw new SemanticError("array index is not int", expr.pos);
        }
        it.type = globalScope.getBaseType(it.typeNode);
    }

    @Override
    public void visit(NullLiteralExpr it) {
        it.type = new PrimitiveType("null");
    }

    @Override
    public void visit(PrefixExpr it) {
        it.src.accept(this);
        switch (it.op) {
            case "!":
                if (!it.src.type.isBool())
                    throw new SemanticError("src is not assignable", it.src.pos);
                break;
            case "~":
            case "+":
            case "-":
                if (!it.src.type.isInt())
                    throw new SemanticError("src is not int", it.pos);
                break;
            case "++":
            case "--":
                if (!it.src.type.isInt())
                    throw new SemanticError("src is not int", it.pos);
                if (!it.src.assignable)
                    throw new SemanticError("src is not assignable", it.pos);
                break;
            default:
                break;
        }
        it.type = it.src.type;
    }

    @Override
    public void visit(StringLiteralExpr it) {
        it.type = new PrimitiveType("string");
    }

    @Override
    public void visit(SuffixExpr it) {
        it.src.accept(this);
        if (!it.src.type.isInt())
            throw new SemanticError("not an int value", it.pos);
        if (!it.src.assignable)
            throw new SemanticError("not assignable", it.pos);
        it.type = it.src.type;
    }

    @Override
    public void visit(ThisExpr it) {
        if (currentClass == null)
            throw new SemanticError("ThisExpr outside class", it.pos);
        it.classEntity = currentClass;
        it.type = currentClass;
    }

    @Override
    public void visit(VarExpr it) {
        it.varEntity = currentScope.getVar(it.identifier, it.pos, true);
        it.type = it.varEntity.type;
    }

    @Override
    public void visit(BlockStmt it) {
        for (BaseStmt stmt : it.stmts) {
            if (stmt instanceof BlockStmt) {
                currentScope = new BaseScope(currentScope);
                stmt.accept(this);
                currentScope = currentScope.parentScope;
            } else stmt.accept(this);
        }
    }

    @Override
    public void visit(BreakStmt it) {
        if (loops.isEmpty())
            throw new SemanticError("break outside loops", it.pos);
    }

    @Override
    public void visit(ContinueStmt it) {
        if (loops.isEmpty())
            throw new SemanticError("continue outside loops", it.pos);
    }

    @Override
    public void visit(EmptyStmt it) {
        //Empty
    }

    @Override
    public void visit(ForStmt it) {
        if (it.init != null) it.init.accept(this);
        if (it.condition != null) {
            it.condition.accept(this);
            if (!it.condition.type.isBool())
                throw new SemanticError("condition is not a bool", it.pos);
        }
        if (it.incr != null) it.incr.accept(this);
        currentScope = new BaseScope(currentScope);
        loops.push(it);
        it.stmts.accept(this);
        loops.pop();
        currentScope = currentScope.parentScope;
    }

    @Override
    public void visit(IfStmt it) {
        it.condition.accept(this);
        if (!it.condition.type.isBool())
            throw new SemanticError("condition is not a bool", it.pos);
        currentScope = new BaseScope(currentScope);
        it.trueStmt.accept(this);
        currentScope = currentScope.parentScope;
        if (it.falseStmt != null) {
            currentScope = new BaseScope(currentScope);
            it.falseStmt.accept(this);
            currentScope = currentScope.parentScope;
        }
    }

    @Override
    public void visit(PureExprStmt it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(ReturnStmt it) {
        if (it.expr != null) {
            it.expr.accept(this);
            if (!it.expr.type.equals(returnType))
                throw new SemanticError("wrong return type", it.pos);
        } else if (!returnType.isVoid())
            throw new SemanticError("missing return value", it.pos);
        hasReturn = true;
    }

    @Override
    public void visit(VarDefStmt it) {
        it.vars.forEach(var -> var.accept(this));
    }

    @Override
    public void visit(VarDefSubStmt it) {
        BaseType varType = globalScope.getBaseType(it.type);
//        if (!globalScope.containClass(it.type.name))
//            throw new SemanticError("undefined type", it.pos);
        if (varType.isVoid())
            throw new SemanticError("void variable", it.pos);
        if (it.init != null) {
            it.init.accept(this);
            if (!it.init.type.equals(varType))
                throw new SemanticError("mismatch type", it.init.pos);
        }
        VarEntity varEntity = new VarEntity(varType, it.name, currentScope == globalScope);
        it.varEntity = varEntity;
        currentScope.defineVar(it.name, varEntity, it.pos);
    }

    @Override
    public void visit(WhileStmt it) {
        if (it.condition != null) {
            it.condition.accept(this);
            if (!it.condition.type.isBool())
                throw new SemanticError("condition is not a bool", it.pos);
        }
        currentScope = new BaseScope(currentScope);
        loops.push(it);
        it.stmts.accept(this);
        loops.pop();
        currentScope = currentScope.parentScope;
    }
}
