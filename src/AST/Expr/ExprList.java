package AST.Expr;

import AST.ASTNode;
import AST.ASTVisitor;
import Util.Position;

import java.util.ArrayList;

public class ExprList extends ASTNode {
    public ArrayList<BaseExpr> exprs = new ArrayList<>();

    public ExprList(ArrayList<BaseExpr> exprs, Position pos) {
        super(pos);
        this.exprs = exprs;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
