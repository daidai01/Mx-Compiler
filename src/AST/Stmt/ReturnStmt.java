package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import Util.Position;

public class ReturnStmt extends BaseStmt {
    public BaseExpr expr;

    public ReturnStmt(BaseExpr expr, Position pos) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
