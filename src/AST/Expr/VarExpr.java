package AST.Expr;

import AST.ASTVisitor;
import Util.Entity.VarEntity;
import Util.Position;

public class VarExpr extends BaseExpr {
    public String identifier;
    public VarEntity varEntity = null;

    public VarExpr(String identifier, Position pos) {
        super(pos);
        this.identifier = identifier;
        assignable = true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
