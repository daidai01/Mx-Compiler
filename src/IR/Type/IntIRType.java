package IR.Type;

public class IntIRType extends BaseIRType {
    public int size;

    public IntIRType(int size) {
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof IntIRType && size == ((IntIRType) other).size;
    }

    @Override
    public String toString() {
        return "i" + size;
    }
}
