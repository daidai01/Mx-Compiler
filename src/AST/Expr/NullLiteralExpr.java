package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class NullLiteralExpr extends BaseExpr {
    public NullLiteralExpr(Position pos) {
        super(pos);
        assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
