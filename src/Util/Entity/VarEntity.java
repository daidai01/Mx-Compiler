package Util.Entity;

import IR.Operand.BaseOperand;
import IR.Operand.IntOperand;
import Util.Type.BaseType;

public class VarEntity extends BaseEntity {
    public boolean isGlobal = false;
    public boolean isMember = false;
    public BaseOperand operand = null;
    public IntOperand index = null;

    public VarEntity(BaseType type, String name, boolean isGlobal) {
        super(type, name);
        this.isGlobal = isGlobal;
    }
}
