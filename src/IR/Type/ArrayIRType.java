package IR.Type;

public class ArrayIRType extends BaseIRType {
    public BaseIRType type;
    public int num;

    public ArrayIRType(BaseIRType type, int num) {
        super();
        this.type = type;
        this.num = num;
    }

    @Override
    public int size() {
        return type.size();
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof ArrayIRType && type.equals(((ArrayIRType) other).type)
                || (other instanceof PointerIRType && (type.equals(((PointerIRType) other).basicType) || other instanceof VoidIRType));
    }

    @Override
    public String toString() {
        return "[" + num + " x " + type.toString() + "]";
    }
}
