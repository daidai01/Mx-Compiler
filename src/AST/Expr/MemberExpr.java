package AST.Expr;

import AST.ASTVisitor;
import Util.Entity.VarEntity;
import Util.Position;

public class MemberExpr extends BaseExpr {
    public BaseExpr expr;
    public String identifier;
    public boolean isFunc = false;
    public VarEntity varEntity = null;

    public MemberExpr(BaseExpr expr, String identifier, Position pos) {
        super(pos);
        this.expr = expr;
        this.identifier = identifier;
        assignable = true;
    }

    public void setIsFunc() {
        this.isFunc = true;
        assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
