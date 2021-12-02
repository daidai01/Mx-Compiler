package IR.Type;

public class IntIRType extends BaseIRType {
    public int size;

    public IntIRType(int size) {
        super();
        this.size = size;
    }

    @Override
    public int getBytes() {
        return size;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof IntIRType && size == ((IntIRType) other).size;
    }
}
