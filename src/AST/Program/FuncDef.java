package AST.Program;

import AST.ASTVisitor;
import AST.Stmt.BlockStmt;
import AST.Stmt.VarDefSubStmt;
import Util.Entity.FuncEntity;
import Util.Position;

import java.util.ArrayList;

public class FuncDef extends BaseDef {
    //    public String name;
    public TypeNode type;
    public boolean isConstructor = false;
    public BlockStmt block;
    public ArrayList<VarDefSubStmt> paras = new ArrayList<>();
    public FuncEntity funcEntity = null;

    public FuncDef(Position pos, String name, TypeNode type, BlockStmt block, ArrayList<VarDefSubStmt> paras) {
        super(name, pos);
        this.type = type;
        this.block = block;
        this.paras = paras;
    }

    public FuncDef(Position pos, String name, TypeNode type, BlockStmt block, ArrayList<VarDefSubStmt> paras, boolean isConstructor) {
        super(name, pos);
//        this.name = name;
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
