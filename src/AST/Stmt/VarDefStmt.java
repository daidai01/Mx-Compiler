package AST.Stmt;

import AST.ASTVisitor;
import Util.Position;

import java.util.ArrayList;

public class VarDefStmt extends BaseStmt {
    public ArrayList<VarDefSubStmt> vars = new ArrayList<>();

    public VarDefStmt(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
