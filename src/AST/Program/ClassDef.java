package AST.Program;

import AST.ASTVisitor;
import AST.Stmt.VarDefSubStmt;
import Util.Position;

import java.util.ArrayList;

public class ClassDef extends BaseDef {
//    public String name;
    public boolean hasConstructor = false;
    public ArrayList<VarDefSubStmt> varDefs = new ArrayList<>();
    public ArrayList<FuncDef> funcDefs = new ArrayList<>();
    public ArrayList<FuncDef> constructors = new ArrayList<>();

    public ClassDef(Position pos, String name, boolean hasConstructor, ArrayList<VarDefSubStmt> varDefs, ArrayList<FuncDef> funcDefs, ArrayList<FuncDef> constructors) {
        super(name, pos);
        this.hasConstructor = hasConstructor;
        this.varDefs = varDefs;
        this.funcDefs = funcDefs;
        this.constructors = constructors;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
