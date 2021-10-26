package AST.Program;

import AST.ASTVisitor;
import AST.Stmt.BlockStmt;
import AST.Stmt.VarDefSubStmt;
import Util.Position;

import java.util.ArrayList;

public class FunDef extends BaseDef {
    public String name;
    public TypeNode type;
    public boolean isConstructor = false;
    public BlockStmt block;
    public ArrayList<VarDefSubStmt> paras = new ArrayList<>();

    public FunDef(Position pos, String name, TypeNode type, boolean isConstructor, BlockStmt block, ArrayList<VarDefSubStmt> paras) {
        super(name, pos);
        this.type = type;
        this.isConstructor = isConstructor;
        this.block = block;
        this.paras = paras;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
