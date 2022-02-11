package Frontend;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.ClassDef;
import AST.Program.FuncDef;
import AST.Program.RootNode;
import AST.Program.TypeNode;
import AST.Stmt.*;
import IR.Operand.IntOperand;
import IR.Type.BaseIRType;
import IR.Type.ClassIRType;
import IR.Type.PointerIRType;
import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Entity.VarEntity;
import Util.Error.SemanticError;
import Util.Scope.BaseScope;
import Util.Scope.ClassScope;
import Util.Scope.GlobalScope;
import Util.Type.ArrayType;
import Util.Type.BaseType;
import Util.Type.PrimitiveType;
import com.sun.jdi.ClassType;

import java.util.ArrayList;
import java.util.Stack;

public class SemanticChecker implements ASTVisitor {
    public GlobalScope globalScope = null;
    public BaseScope currentScope = null;
    public ClassEntity currentClass = null;
    public FuncEntity currentFunc = null;
    public BaseType returnType = null;
    public boolean hasReturn = false;
    public Stack<ASTNode> loops = new Stack<>();

    //Lambda
    public int isLambda = 0;
    public BaseType lambdaReturnType = null;
    public boolean lambdaHasReturn = false;

    //IR
    public IR.Program.IRRoot IRRoot = null;
    public ClassIRType currentIRClass = null;

    public SemanticChecker(GlobalScope globalScope, IR.Program.IRRoot IRRoot) {
        this.globalScope = globalScope;
        this.IRRoot = IRRoot;
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
        currentIRClass = IRRoot.types.get(it.name);
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
        returnType = globalScope.getClass("void", it.pos);
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
        if ((it.leftSrc.type.isBool() || it.leftSrc.type.isInt()) && it.rightSrc.type.isNull())
            throw new SemanticError("null cannot be assigned to primitive types", it.pos);
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
                it.type = globalScope.getClass("int", it.pos);
                break;
            case "+":
                if (it.src1.type.isInt() && it.src2.type.isInt())
                    it.type = globalScope.getClass("int", it.pos);
                else if (it.src1.type.isString() && it.src2.type.isString())
                    it.type = globalScope.getClass("string", it.pos);
                else throw new SemanticError("there can only be pairs of int and string values", it.pos);
                break;
            case ">":
            case "<":
            case ">=":
            case "<=":
                if (it.src1.type.isInt() && it.src2.type.isInt()) ;
                else if (it.src1.type.isString() && it.src2.type.isString()) ;
                else throw new SemanticError("there can only be pairs of int and string values", it.pos);
                it.type = globalScope.getClass("bool", it.pos);
                break;
            case "==":
            case "!=":
                if (it.src1.type.equals(it.src2.type)) ;
                else if (it.src1.type.typeName.equals("null") && !it.src2.type.isPrimitive()) ;
                else if (it.src2.type.typeName.equals("null") && !it.src1.type.isPrimitive()) ;
                else throw new SemanticError("type mismatch", it.pos);
                it.type = globalScope.getClass("bool", it.pos);
                break;
            case "&&":
            case "||":
                if (!it.src1.type.isBool() || !it.src2.type.isBool())
                    throw new SemanticError("there can only be bool values", it.pos);
                else it.type = globalScope.getClass("bool", it.pos);
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(BoolLiteralExpr it) {
        it.type = globalScope.getClass("bool", it.pos);
    }

    @Override
    public void visit(ExprList it) {
    }

    @Override
    public void visit(FuncCallExpr it) {
        it.name.accept(this);
        //TODO check this
//        System.out.println(it.name.type);
        if (!(it.name.type instanceof FuncEntity))
            throw new SemanticError("not a function", it.pos);
        FuncEntity funcEntity = (FuncEntity) it.name.type;
        ArrayList<BaseExpr> givenParas = it.paras;
        ArrayList<VarEntity> requiredParas = funcEntity.scope.paras;
        if (givenParas.size() != requiredParas.size())
            throw new SemanticError("wrong number of parameters", it.pos);
        for (int i = 0; i < givenParas.size(); ++i) {
            givenParas.forEach(para -> para.accept(this));
            if (givenParas.get(i).type.equals(requiredParas.get(i).type)) ;
            else if (givenParas.get(i).type.typeName.equals("null") && !requiredParas.get(i).type.isPrimitive()) ;
            else throw new SemanticError("type mismatch", givenParas.get(i).pos);
        }
        it.type = it.name.type;
        if (!it.type.isArray() && globalScope.containClass(((FuncEntity) it.type).type.typeName))
            it.type = globalScope.getClass(((FuncEntity) it.type).type.typeName, it.pos);
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
        else if (globalScope.containClass(it.identifier.type.typeName))
            it.type = globalScope.getClass(it.identifier.type.typeName, it.pos);
        else it.type = new PrimitiveType(it.identifier.type.typeName);
    }

    @Override
    public void visit(IntLiteralExpr it) {
        it.type = globalScope.getClass("int", it.pos);
        ;
    }

    @Override
    public void visit(LambdaExpr it) {
        if (it.paras.size() != it.exprs.size())
            throw new SemanticError("different number of lambda paras and exprs", it.pos);
        currentScope = new BaseScope(currentScope);
        isLambda++;
        it.paras.forEach(para -> para.accept(this));
        it.exprs.forEach(expr -> expr.accept(this));
        if (!it.paras.isEmpty()) {
            for (int i = 0; i < it.paras.size(); ++i) {
                BaseType paraType = globalScope.getClass(it.paras.get(i).type);
                BaseType exprType = globalScope.getClass(it.exprs.get(i).type.typeName, it.exprs.get(i).pos);
                if (!paraType.equals(exprType))
                    throw new SemanticError("expr type different from para in lambda", it.exprs.get(i).pos);
            }
        }
        lambdaHasReturn = false;
        it.block.accept(this);
        if (lambdaHasReturn) {
//            if (lambdaReturnType == null)
//                throw new SemanticError("no return", it.pos);
//            else
            it.type = lambdaReturnType;
        } else it.type = globalScope.getClass("void", it.pos);
        isLambda--;
        lambdaHasReturn = false;
//        lambdaReturnType = null;
        currentScope = currentScope.parentScope;
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
                throw new SemanticError(it.expr.type + ": not a class", it.expr.pos);
            ClassEntity classEntity = (ClassEntity) it.expr.type;
            if (classEntity.scope.containFunc(it.identifier, false))
                it.type = classEntity.scope.getFunc(it.identifier, it.pos, false);
            else throw new SemanticError(it.identifier + ": no such function", it.pos);
        } else {
            if (!it.expr.type.isClass())
                throw new SemanticError("not a class", it.expr.pos);
//            ClassEntity classEntity;
//            if (it.expr.type instanceof ClassEntity)
            ClassEntity classEntity = (ClassEntity) it.expr.type;
//            else classEntity = new ClassEntity(it.expr.type, it.expr.type.typeName, null);
            if (classEntity.scope.containVar(it.identifier, false)) {
                it.varEntity = classEntity.scope.getVar(it.identifier, it.pos, false);
                it.type = it.varEntity.type;
                if (!it.type.isArray() && globalScope.containClass(it.type.typeName))
                    it.type = globalScope.getClass(it.type.typeName, it.pos);
            } else throw new SemanticError("no such member", it.pos);
        }
    }

    @Override
    public void visit(NewExpr it) {
        for (BaseExpr expr : it.exprs) {
            expr.accept(this);
            if (!expr.type.isInt())
                throw new SemanticError("array index is not int", expr.pos);
        }
        it.type = globalScope.getClass(it.typeNode);
    }

    @Override
    public void visit(NullLiteralExpr it) {
        it.type = globalScope.getClass("null", it.pos);
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
        it.type = globalScope.getClass("string", it.pos);
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
        if (it.varEntity.type.isArray()) it.type = it.varEntity.type;
        else it.type = globalScope.getClass(it.varEntity.typeName, it.pos);
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
        it.target = loops.peek();
    }

    @Override
    public void visit(ContinueStmt it) {
        if (loops.isEmpty())
            throw new SemanticError("continue outside loops", it.pos);
        it.target = loops.peek();
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
        if (isLambda != 0) {
            if (it.expr != null) {
                it.expr.accept(this);
                lambdaReturnType = it.expr.type;
            } else lambdaReturnType = globalScope.getClass("void", it.pos);
            lambdaHasReturn = true;
        } else {
            if (it.expr != null) {
                it.expr.accept(this);
//            System.out.println(it.expr.type.typeName);
//            System.out.println(it.expr.type.dim);//1
//            System.out.println(returnType.typeName);
//            System.out.println(returnType.dim);//0
                if (!((BaseType) it.expr.type).equals(returnType))
                    throw new SemanticError("wrong return type", it.pos);
            } else if (!returnType.isVoid())
                throw new SemanticError("missing return value", it.pos);
            hasReturn = true;
        }
    }

    @Override
    public void visit(VarDefStmt it) {
        it.vars.forEach(var -> var.accept(this));
    }

    @Override
    public void visit(VarDefSubStmt it) {
        ClassEntity varType = globalScope.getClass(it.type);
        if (varType.isVoid())
            throw new SemanticError("void variable", it.pos);
        if (currentIRClass != null) {
            BaseIRType irType = IRRoot.getIRType(it.varEntity.type, true);
            if (irType instanceof ClassType) irType = new PointerIRType(irType, false);
            currentIRClass.addMember(irType);
        }
        if (it.init != null) {
            it.init.accept(this);
//            System.out.println(it.init.type.typeName);
//            System.out.println(varType.typeName);
//            System.out.println(it.init.type.dim);//0
//            System.out.println(varType.dim);//1
            if (it.init.type.equals(varType)) ;
            else if ((!varType.typeName.equals("int") || !varType.typeName.equals("bool")) && it.init.type.isNull()) ;
            else throw new SemanticError("mismatch type", it.init.pos);
        }
        VarEntity varEntity = new VarEntity(varType, it.name, currentScope == globalScope);
        if (currentScope instanceof ClassScope)
            varEntity.index = new IntOperand(currentClass.addAllocSize(varEntity.type), 32);
        it.varEntity = varEntity;
        if (globalScope.containClass(it.name))
            throw new SemanticError(it.name + ": conflict with a class", it.pos);
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
