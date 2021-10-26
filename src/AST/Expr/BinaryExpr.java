package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class BinaryExpr extends BaseExpr {
    public BaseExpr src1;
    public BaseExpr src2;
    public String op;

    public BinaryExpr(BaseExpr src1, BaseExpr src2, String op, Position pos) {
        super(pos);
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
