package Util.Entity;

import Util.Type.BaseType;

public class BaseEntity {
    public BaseType type;
    public String name;

    public BaseEntity(BaseType type,String name) {
        this.name = name;
    }
}
