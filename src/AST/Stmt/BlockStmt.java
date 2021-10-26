package AST.Stmt;

import AST.ASTVisitor;
import Util.Position;

import java.util.ArrayList;

public class BlockStmt extends BaseStmt {
    public ArrayList<BaseStmt> stmts = new ArrayList<>();

    public BlockStmt(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
