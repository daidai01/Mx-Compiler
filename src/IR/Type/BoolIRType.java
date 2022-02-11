package IR.Type;

public class BoolIRType extends BaseIRType{
    @Override
    public int size() {
        return 8;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof BoolIRType;
    }

    @Override
    public String toString() {
        return "i1";
    }
}
