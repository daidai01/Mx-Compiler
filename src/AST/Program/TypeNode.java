package AST.Program;

import AST.ASTNode;
import AST.ASTVisitor;
import Util.Position;
import Util.Type.BaseType;

public class TypeNode extends ASTNode {
    public String name;
    public int dim;
    public BaseType baseType;//TODO necessary or not?

    public TypeNode(Position pos, String name, int dim) {
        super(pos);
        this.name = name;
        this.dim = dim;
        baseType = new BaseType(name);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
