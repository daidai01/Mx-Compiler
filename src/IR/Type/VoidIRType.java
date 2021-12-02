package IR.Type;

public class VoidIRType extends BaseIRType{
    @Override
    public int getBytes() {
        return 0;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof VoidIRType;
    }
}
