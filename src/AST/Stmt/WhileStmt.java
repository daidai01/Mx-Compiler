package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import Util.Position;

public class WhileStmt extends BaseStmt {
    public BaseExpr condition;
    public BaseStmt stmts;

    public WhileStmt(BaseExpr condition, BaseStmt stmts, Position pos) {
        super(pos);
        this.condition = condition;
        this.stmts = stmts;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
