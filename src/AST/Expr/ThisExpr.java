package AST.Expr;

import AST.ASTVisitor;
import Util.Entity.ClassEntity;
import Util.Position;

public class ThisExpr extends BaseExpr {
    public ClassEntity classEntity = null;

    public ThisExpr(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
