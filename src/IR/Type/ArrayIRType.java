package IR.Type;

public class ArrayIRType extends BaseIRType {
    public BaseIRType type;
    public int size;

    public ArrayIRType(BaseIRType type, int size) {
        super();
        this.type = type;
        this.size = size;
    }

    @Override
    public int getBytes() {
        return type.getBytes() * size;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof ArrayIRType && type.equals(((ArrayIRType) other).type) && size == ((ArrayIRType) other).size;
    }
}
