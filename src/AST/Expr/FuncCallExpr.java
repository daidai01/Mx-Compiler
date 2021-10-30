package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

import java.util.ArrayList;

public class FuncCallExpr extends BaseExpr {
    public BaseExpr name;
    public ArrayList<BaseExpr> paras = new ArrayList<>();

    public FuncCallExpr(BaseExpr name, ArrayList<BaseExpr> paras, Position pos) {
        super(pos);
        this.name = name;
        if (paras != null) this.paras = paras;
        assignable = false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
