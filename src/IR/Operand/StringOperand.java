package IR.Operand;

import IR.Instruction.BaseInst;
import IR.Type.ArrayIRType;
import IR.Type.BaseIRType;
import IR.Type.IntIRType;
import IR.Type.PointerIRType;

import java.util.HashSet;

public class StringOperand extends BaseOperand {
    public String name;
    public String value;

    public StringOperand(String name, String value) {
        super(new PointerIRType(new ArrayIRType(new IntIRType(8), value.length()), true));
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public HashSet<BaseInst> getUses() {
        return new HashSet<>();
    }

    @Override
    public void addUse(BaseInst inst) {

    }

    @Override
    public void removeUse(BaseInst inst) {

    }

    public String toIRString(){
        String str = value.replace("\\", "\\5C");
        str = str.replace("\n", "\\0A");
        str = str.replace("\0", "\\00");
        str = str.replace("\t", "\\09");
        str = str.replace("\"", "\\22");
        return str;
    }
}
