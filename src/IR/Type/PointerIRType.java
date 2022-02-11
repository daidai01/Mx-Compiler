package IR.Type;

public class PointerIRType extends BaseIRType {
    public BaseIRType basicType;
    public int dim;
    public boolean resolvable = false;

    public PointerIRType(BaseIRType basicType, boolean resolvable) {
        super();
        this.basicType = basicType;
        this.dim = basicType.dim() + 1;
        this.resolvable = resolvable;
    }

    @Override
    public int size() {
        return 32;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return (other instanceof PointerIRType && (basicType.equals(((PointerIRType) other).basicType) || ((PointerIRType) other).basicType instanceof VoidIRType))
                || (other instanceof ArrayIRType && other.equals(this));
    }

    @Override
    public String toString() {
        if (basicType instanceof VoidIRType) return "";
        else return basicType.toString() + "*";
    }
}
