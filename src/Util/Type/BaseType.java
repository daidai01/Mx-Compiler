package Util.Type;

import Util.Entity.ClassEntity;

public class BaseType {
    public String typeName;
    public int dim = 0;

    public BaseType(String typeName) {
        this.typeName = typeName;
    }

    public BaseType(String typeName, int dim) {
        if (typeName != null) this.typeName = typeName;
        this.dim = dim;
    }

    public boolean equals(BaseType other) {
        if (typeName.equals(other.typeName) && dim == other.dim) return true;
        else if (typeName.equals("null") && !other.isPrimitive()) return true;
        else if (other.typeName.equals("null") && !isPrimitive()) return true;
        else return false;
    }

    public boolean isBool() {
        return typeName.equals("bool") && dim == 0;
    }

    public boolean isInt() {
        return typeName.equals("int") && dim == 0;
    }

    public boolean isVoid() {
        return typeName.equals("void") && dim == 0;
    }

    public boolean isString() {
        return typeName.equals("string") && dim == 0;
    }

    public boolean isNull() {
        return typeName.equals("null") && dim == 0;
    }

    public boolean isArray() {
        return dim > 0;
    }

    public boolean isClass() {
        //TODO is this true?
        return this instanceof ClassEntity;
    }

    public boolean isPrimitive() {
        return this.isInt() || this.isBool();
    }
}
