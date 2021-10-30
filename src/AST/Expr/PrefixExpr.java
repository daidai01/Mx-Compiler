package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class PrefixExpr extends BaseExpr {
    public BaseExpr src;
    public String op;

    public PrefixExpr(BaseExpr src, String op, Position pos) {
        super(pos);
        this.src = src;
        this.op = op;
        if (op.equals("++") || op.equals("--")) assignable = true;
        else assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
