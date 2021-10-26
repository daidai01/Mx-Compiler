package AST.Program;

import AST.ASTVisitor;
import AST.Stmt.VarDefStmt;
import Util.Position;

import java.util.ArrayList;

public class ClassDef extends BaseDef {
    public String name;
    public boolean hasConstructor = false;
    public ArrayList<VarDefStmt> varDefs = new ArrayList<>();
    public ArrayList<FunDef> funcDefs = new ArrayList<>();
    public ArrayList<FunDef> constructors = new ArrayList<>();

    public ClassDef(Position pos, String name) {
        super(name, pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
