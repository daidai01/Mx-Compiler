package AST.Stmt;

import AST.ASTNode;
import Util.Position;

abstract public class BaseStmt extends ASTNode {
    public BaseStmt(Position pos) {
        super(pos);
    }
}
