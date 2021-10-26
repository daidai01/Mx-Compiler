package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import Util.Position;

public class PureExprStmt extends BaseStmt {
    public BaseExpr expr;

    public PureExprStmt(BaseExpr expr, Position pos) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
