package AST.Stmt;

import AST.ASTVisitor;
import Util.Position;

public class EmptyStmt extends BaseStmt {
    public EmptyStmt(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
