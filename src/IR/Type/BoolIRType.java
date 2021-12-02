package IR.Type;

public class BoolIRType extends BaseIRType{
    @Override
    public int getBytes() {
        return 8;
    }



    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof BoolIRType;
    }
}
