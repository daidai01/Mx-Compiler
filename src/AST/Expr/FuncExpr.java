package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class FuncExpr extends BaseExpr {
    public String name;

    public FuncExpr(String name, Position pos) {
        super(pos);
        this.name = name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
