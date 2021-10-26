package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

import java.util.ArrayList;

public class FunctionExpr extends BaseExpr {
    public BaseExpr name;
    public ArrayList<BaseExpr> paras = new ArrayList<>();

    public FunctionExpr(BaseExpr name, ArrayList<BaseExpr> paras, Position pos) {
        super(pos);
        this.name = name;
        this.paras = paras;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
