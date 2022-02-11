package IR.Type;

public class VoidIRType extends BaseIRType{
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof VoidIRType;
    }

    @Override
    public String toString() {
        return "void";
    }
}
