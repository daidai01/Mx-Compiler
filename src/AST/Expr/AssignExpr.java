package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class AssignExpr extends BaseExpr {
    public BaseExpr leftSrc;
    public BaseExpr rightSrc;

    public AssignExpr(BaseExpr leftSrc, BaseExpr rightSrc, Position pos) {
        super(pos);
        this.leftSrc = leftSrc;
        this.rightSrc = rightSrc;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
