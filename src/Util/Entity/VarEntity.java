package Util.Entity;

import Util.Type.BaseType;

public class VarEntity extends BaseEntity {
    public boolean isGlobal = false;
    public boolean isMember = false;

    public VarEntity(BaseType type, String name, boolean isGlobal) {
        super(type, name);
        this.isGlobal = isGlobal;
    }
}
