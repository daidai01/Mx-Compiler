package Frontend;

import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.ClassDef;
import AST.Program.FunDef;
import AST.Program.RootNode;
import AST.Program.TypeNode;
import AST.Stmt.*;
import Util.Scope.GlobalScope;

public class SymbolCollector implements ASTVisitor {
    public GlobalScope globalScope;

    public SymbolCollector(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {

    }

    @Override
    public void visit(ClassDef it) {

    }

    @Override
    public void visit(FunDef it) {

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
    public void visit(FunctionExpr it) {

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
