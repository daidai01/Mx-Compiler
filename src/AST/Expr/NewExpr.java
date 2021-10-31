package AST.Expr;

import AST.ASTVisitor;
import AST.Program.TypeNode;
import Util.Position;

import java.util.ArrayList;

public class NewExpr extends BaseExpr {
    public TypeNode typeNode;
    public ArrayList<BaseExpr> exprs;

    public NewExpr(TypeNode typeNode, ArrayList<BaseExpr> exprs, Position pos) {
        super(pos);
        this.typeNode = typeNode;
        this.exprs = exprs;
        assignable = true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
