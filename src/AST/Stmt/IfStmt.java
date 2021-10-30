package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import Util.Position;

public class IfStmt extends BaseStmt {
    public BaseExpr condition;
    public BaseStmt trueStmt;
    public BaseStmt falseStmt;

    public IfStmt(BaseExpr condition, BaseStmt trueStmt, BaseStmt falseStmt, Position pos) {
        super(pos);
        this.condition = condition;
        this.trueStmt = trueStmt;
        this.falseStmt = falseStmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
