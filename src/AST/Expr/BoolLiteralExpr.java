package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class BoolLiteralExpr extends BaseExpr {
    public boolean value;

    public BoolLiteralExpr(boolean value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
