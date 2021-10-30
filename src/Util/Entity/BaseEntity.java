package Util.Entity;

import Util.Type.BaseType;

public class BaseEntity extends BaseType {
    public BaseType type;
    public String name;

    public BaseEntity(BaseType type, String name) {
        super(type.typeName, type.dim);
        this.type = type;
        this.name = name;
    }
}
