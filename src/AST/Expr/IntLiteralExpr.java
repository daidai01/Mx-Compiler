package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class IntLiteralExpr extends BaseExpr {
    public int value;

    public IntLiteralExpr(int value, Position pos) {
        super(pos);
        this.value = value;
        assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
