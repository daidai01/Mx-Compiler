package IR.Type;

public class IntIRType extends BaseIRType {
    public enum BitWidth {int1, int8, int32}

    public BitWidth bitWidth;

    public IntIRType(BitWidth bitWidth) {
        this.bitWidth = bitWidth;
    }

    @Override
    public int getBytes() {
        if (bitWidth == BitWidth.int1) return 1;
        else if (bitWidth == BitWidth.int8) return 1;
        else return 4;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof IntIRType && bitWidth == ((IntIRType) other).bitWidth;
    }
}
