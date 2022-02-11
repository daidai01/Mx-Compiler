package IR.Type;

abstract public class BaseIRType {
    public BaseIRType() {
    }

    abstract public int size();

    abstract public boolean equals(BaseIRType other);

    abstract public String toString();

    public int dim() {
        if (this instanceof PointerIRType) return ((PointerIRType) this).dim;
        else return 0;
    }

    public boolean resolvable() {
        if (this instanceof PointerIRType) return ((PointerIRType) this).resolvable;
        else return false;
    }
}
