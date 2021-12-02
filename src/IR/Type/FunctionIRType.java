package IR.Type;

import java.util.ArrayList;

public class FunctionIRType extends BaseIRType {
    public BaseIRType type;
    public ArrayList<BaseIRType> paras;

    public FunctionIRType(BaseIRType type, ArrayList<BaseIRType> paras) {
        super();
        this.type = type;
        this.paras = paras;
    }

    @Override
    public int getBytes() {
        return 0;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof FunctionIRType && type.equals(((FunctionIRType) other).type);
    }
}
