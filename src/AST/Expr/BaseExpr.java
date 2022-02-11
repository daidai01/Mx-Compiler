package AST.Expr;

import AST.ASTNode;
import IR.Operand.BaseOperand;
import IR.Program.IRBlock;
import Util.Position;
import Util.Type.BaseType;

abstract public class BaseExpr extends ASTNode {
    public BaseType type;
    public boolean assignable = false;

    public BaseOperand operand = null;
    public BaseOperand address = null;
    public IRBlock thenBlock = null;
    public IRBlock elseBlock = null;

    public BaseExpr(Position pos) {
        super(pos);
    }
}
