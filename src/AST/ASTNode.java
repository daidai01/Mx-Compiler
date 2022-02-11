package AST;

import Util.Entity.VarEntity;
import Util.Position;

import java.util.HashSet;

abstract public class ASTNode {
    public Position pos;
    public HashSet<VarEntity> uses = new HashSet<>();

    public ASTNode(Position pos) {
        this.pos = pos;
    }

    abstract public void accept(ASTVisitor visitor);
}
