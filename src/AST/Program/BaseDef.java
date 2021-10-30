package AST.Program;

import AST.ASTNode;
import Util.Position;

abstract public class BaseDef extends ASTNode {
    public String name;

    public BaseDef(String name, Position pos) {
        super(pos);
        this.name = name;
    }
}
