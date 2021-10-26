package AST.Expr;

import AST.ASTVisitor;
import Util.Position;

public class MemberExpr extends BaseExpr {
    public BaseExpr identifier;
    public String member;

    public MemberExpr(BaseExpr identifier, String member, Position pos) {
        super(pos);
        this.identifier = identifier;
        this.member = member;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
