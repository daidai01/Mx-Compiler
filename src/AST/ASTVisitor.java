package AST;

import AST.Program.*;
import AST.Expr.*;
import AST.Stmt.*;

public interface ASTVisitor {
    //Program
    void visit(RootNode it);

    void visit(ClassDef it);

    void visit(FuncDef it);

    void visit(TypeNode it);

    //Expr
    void visit(AssignExpr it);

    void visit(BinaryExpr it);

    void visit(BoolLiteralExpr it);

    void visit(ExprList it);

    void visit(FuncCallExpr it);

    void visit(FuncExpr it);

    void visit(IndexExpr it);

    void visit(IntLiteralExpr it);

    void visit(LambdaExpr it);

    void visit(MemberExpr it);

    void visit(NewExpr it);

    void visit(NullLiteralExpr it);

    void visit(PrefixExpr it);

    void visit(StringLiteralExpr it);

    void visit(SuffixExpr it);

    void visit(ThisExpr it);

    void visit(VarExpr it);

    //Stmt
    void visit(BlockStmt it);

    void visit(BreakStmt it);

    void visit(ContinueStmt it);

    void visit(EmptyStmt it);

    void visit(ForStmt it);

    void visit(IfStmt it);

    void visit(PureExprStmt it);

    void visit(ReturnStmt it);

    void visit(VarDefStmt it);

    void visit(VarDefSubStmt it);

    void visit(WhileStmt it);
}
