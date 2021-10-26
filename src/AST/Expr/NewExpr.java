package AST.Expr;

import AST.ASTVisitor;
import AST.Program.TypeNode;
import Util.Position;

import java.util.ArrayList;

public class NewExpr extends BaseExpr {
    public TypeNode type;
    public ArrayList<BaseExpr> exprs;

    public NewExpr(TypeNode type, ArrayList<BaseExpr> exprs, Position pos) {
        super(pos);
        this.type = type;
        this.exprs = exprs;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
