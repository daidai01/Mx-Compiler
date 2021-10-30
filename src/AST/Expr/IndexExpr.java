package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class IndexExpr extends BaseExpr {
    public BaseExpr identifier;
    public BaseExpr index;

    public IndexExpr(BaseExpr identifier, BaseExpr index, Position pos) {
        super(pos);
        this.identifier = identifier;
        this.index = index;
        assignable = identifier.assignable;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
