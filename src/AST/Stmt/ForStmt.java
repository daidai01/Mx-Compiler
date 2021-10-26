package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import Util.Position;

public class ForStmt extends BaseStmt {
    public BaseExpr init;
    public BaseExpr condition;
    public BaseExpr incr;
    public BaseStmt stmts;

    public ForStmt(BaseExpr init, BaseExpr condition, BaseExpr incr, BaseStmt stmts, Position pos) {
        super(pos);
        this.init = init;
        this.condition = condition;
        this.incr = incr;
        this.stmts = stmts;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
