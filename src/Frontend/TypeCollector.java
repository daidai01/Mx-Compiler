package Frontend;

import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.*;
import AST.Stmt.*;
import Util.Entity.ClassEntity;
import Util.Entity.VarEntity;
import Util.Error.SemanticError;
import Util.Scope.BaseScope;
import Util.Scope.ClassScope;
import Util.Scope.FuncScope;
import Util.Scope.GlobalScope;

public class TypeCollector implements ASTVisitor {
    public GlobalScope globalScope = null;
    BaseScope currentScope = null;

    public TypeCollector(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        currentScope = globalScope;
        //TODO why !(def instance of VarDefSubStmt)?
        it.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(ClassDef it) {
        ClassEntity classEntity = globalScope.getClass(it.name, it.pos);
        currentScope = classEntity.scope;
        it.varDefs.forEach(var -> var.accept(this));
        it.funcDefs.forEach(func -> func.accept(this));
        it.constructors.forEach(cons -> cons.accept(this));
        currentScope = currentScope.parentScope;
    }

    @Override
    public void visit(FuncDef it) {
        if (it.isConstructor) it.funcEntity.type = null;
        else it.funcEntity.type = globalScope.getClass(it.type);
        currentScope = new FuncScope(currentScope);
        it.paras.forEach(para -> para.accept(this));
        it.funcEntity.scope = (FuncScope) currentScope;
        currentScope = currentScope.parentScope;
    }

    @Override
    public void visit(TypeNode it) {
    }

    @Override
    public void visit(AssignExpr it) {
    }

    @Override
    public void visit(BinaryExpr it) {
    }

    @Override
    public void visit(BoolLiteralExpr it) {
    }

    @Override
    public void visit(ExprList it) {
    }

    @Override
    public void visit(FuncCallExpr it) {
    }

    @Override
    public void visit(FuncExpr it) {
    }

    @Override
    public void visit(IndexExpr it) {
    }

    @Override
    public void visit(IntLiteralExpr it) {
    }

    @Override
    public void visit(LambdaExpr it) {
    }

    @Override
    public void visit(MemberExpr it) {
    }

    @Override
    public void visit(NewExpr it) {
    }

    @Override
    public void visit(NullLiteralExpr it) {
    }

    @Override
    public void visit(PrefixExpr it) {
    }

    @Override
    public void visit(StringLiteralExpr it) {
    }

    @Override
    public void visit(SuffixExpr it) {
    }

    @Override
    public void visit(ThisExpr it) {
    }

    @Override
    public void visit(VarExpr it) {
    }

    @Override
    public void visit(BlockStmt it) {
    }

    @Override
    public void visit(BreakStmt it) {
    }

    @Override
    public void visit(ContinueStmt it) {
    }

    @Override
    public void visit(EmptyStmt it) {
    }

    @Override
    public void visit(ForStmt it) {
    }

    @Override
    public void visit(IfStmt it) {
    }

    @Override
    public void visit(PureExprStmt it) {
    }

    @Override
    public void visit(ReturnStmt it) {
    }

    @Override
    public void visit(VarDefStmt it) {
        it.vars.forEach(var -> var.accept(this));
    }

    @Override
    public void visit(VarDefSubStmt it) {
        VarEntity varEntity = new VarEntity(globalScope.getBaseType(it.type), it.name, false);
        if (varEntity.type.isVoid())
            throw new SemanticError("a parameter should have a type", it.pos);
        it.varEntity = varEntity;
        if (currentScope instanceof FuncScope)
            ((FuncScope) currentScope).addPara(varEntity, it.pos);
        else if (currentScope instanceof ClassScope)
            ((ClassScope) currentScope).defineVar(varEntity.name, varEntity, it.pos);
    }

    @Override
    public void visit(WhileStmt it) {
    }
}
