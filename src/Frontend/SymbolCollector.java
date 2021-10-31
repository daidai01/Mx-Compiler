package Frontend;

import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.ClassDef;
import AST.Program.FuncDef;
import AST.Program.RootNode;
import AST.Program.TypeNode;
import AST.Stmt.*;
import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Error.SemanticError;
import Util.Scope.BaseScope;
import Util.Scope.ClassScope;
import Util.Scope.GlobalScope;

public class SymbolCollector implements ASTVisitor {
    public GlobalScope globalScope = null;
    public BaseScope currentScope = null;

    public SymbolCollector(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        currentScope = globalScope;
        it.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(ClassDef it) {
        currentScope = new ClassScope(currentScope);
        ClassEntity classEntity = new ClassEntity(it.name);
        globalScope.defineClass(it.name, classEntity, it.pos);
        it.varDefs.forEach(var -> var.accept(this));
        it.funcDefs.forEach(func -> func.accept(this));
        it.constructors.forEach(cons -> cons.accept(this));
        classEntity.scope = (ClassScope) currentScope;
        currentScope = currentScope.parentScope;
        if (globalScope.containFunc(it.name, false))
            throw new SemanticError(it.name + ": conflict with a function", it.pos);
    }

    @Override
    public void visit(FuncDef it) {
        FuncEntity funcEntity = new FuncEntity(null, it.name);
        it.funcEntity = funcEntity;
        currentScope.defineFunc(it.name, funcEntity, it.pos);
        if (!it.isConstructor && globalScope.containClass(it.name))
            throw new SemanticError(it.name + ": conflict with a class", it.pos);
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
    }

    @Override
    public void visit(VarDefSubStmt it) {
    }

    @Override
    public void visit(WhileStmt it) {
    }
}
