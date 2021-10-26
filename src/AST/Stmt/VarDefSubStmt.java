package AST.Stmt;

import AST.ASTVisitor;
import AST.Expr.BaseExpr;
import AST.Program.TypeNode;
import Util.Position;

public class VarDefSubStmt extends BaseStmt {
    public TypeNode type;
    public String name;
    public BaseExpr init;

    public VarDefSubStmt(TypeNode type, String name, BaseExpr init, Position pos) {
        super(pos);
        this.type = type;
        this.name = name;
        this.init = init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
