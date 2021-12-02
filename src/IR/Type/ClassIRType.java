package IR.Type;

import java.util.ArrayList;

public class ClassIRType extends BaseIRType {
    public String name;
    public ArrayList<BaseIRType> members;

    public ClassIRType(String name, ArrayList<BaseIRType> members) {
        super();
        this.name = name;
        this.members = members;
    }

    @Override
    public int getBytes() { //todo is this it?
        int bytes = 0;
        for (var member : members) {
            bytes += member.getBytes();
        }
        return bytes;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof ClassIRType && name.equals(((ClassIRType) other).name);
    }
}
