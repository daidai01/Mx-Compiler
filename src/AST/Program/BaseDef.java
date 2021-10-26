package AST.Program;

import AST.ASTNode;
import Util.Position;

abstract public class BaseDef extends ASTNode {
    String name;

    public BaseDef(String name, Position pos) {
        super(pos);
        this.name = name;
    }
}
