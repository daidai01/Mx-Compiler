package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class StringLiteralExpr extends BaseExpr {
    public String value;

    public StringLiteralExpr(String value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
