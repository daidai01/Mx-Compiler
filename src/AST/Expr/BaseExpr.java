package AST.Expr;

import AST.ASTNode;
import Util.Position;
import Util.Type.BaseType;

abstract public class BaseExpr extends ASTNode {
    public BaseType type;
    public boolean assignable = false;

    public BaseExpr(Position pos) {
        super(pos);
    }
}
