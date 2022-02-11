package AST.Stmt;

import AST.ASTNode;
import AST.ASTVisitor;
import Util.Position;

public class ContinueStmt extends BaseStmt {
    public ASTNode target = null;

    public ContinueStmt(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
