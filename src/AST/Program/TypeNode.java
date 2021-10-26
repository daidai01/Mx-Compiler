package AST.Program;

import AST.ASTNode;
import AST.ASTVisitor;
import Util.Position;

public class TypeNode extends ASTNode {
    public String type;
    public int dim;

    public TypeNode(Position pos, String type, int dim) {
        super(pos);
        this.type = type;
        this.dim = dim;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
