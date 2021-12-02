package IR.Type;

public class PointerIRType extends BaseIRType {
    public BaseIRType type;

    public PointerIRType(BaseIRType type) {
        super();
        this.type = type;
    }

    @Override
    public int getBytes() {
        return 4;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof PointerIRType && (type.equals(((PointerIRType) other).type) || other instanceof VoidIRType);
    }
}
