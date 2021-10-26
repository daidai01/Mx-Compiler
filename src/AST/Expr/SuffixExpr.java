package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class SuffixExpr extends BaseExpr{
    public BaseExpr src;
    public String op;

    public SuffixExpr(BaseExpr src, String op, Position pos) {
        super(pos);
        this.src = src;
        this.op = op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
