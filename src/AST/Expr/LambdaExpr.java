package AST.Expr;

import AST.ASTVisitor;
import AST.Stmt.BlockStmt;
import AST.Stmt.VarDefSubStmt;
import Util.Position;

import java.util.ArrayList;

public class LambdaExpr extends BaseExpr {
    //[&](paraList) -> {stmts}(exprList)
    public ArrayList<VarDefSubStmt> paras = new ArrayList<>();
    public BlockStmt block;
    public ArrayList<BaseExpr> exprs = new ArrayList<>();

    public LambdaExpr(ArrayList<VarDefSubStmt> paras, BlockStmt block, ArrayList<BaseExpr> exprs, Position pos) {
        super(pos);
        this.paras = paras;
        this.block = block;
        this.exprs = exprs;
        assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
