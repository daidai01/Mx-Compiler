package Util.Type;

abstract public class BaseType {
    public String typeName;
    public int dim = 0;

    public BaseType(String typeName) {
        this.typeName = typeName;
    }

    public boolean equals(BaseType other) {
        return typeName.equals(other.typeName) && dim == other.dim;
    }

    public boolean isBool() {
        return false;
    }

    public boolean isInt() {
        return false;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isArray() {
        return dim > 0;
    }

    public boolean isClass() {
        return false;
    }

    public boolean isFunc(){
        return false;
    }

    public boolean isConstructor(){
        return false;
    }
}
