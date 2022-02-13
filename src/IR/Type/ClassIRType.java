package IR.Type;

import java.util.ArrayList;

public class ClassIRType extends BaseIRType {
    public String name;
    public ArrayList<BaseIRType> members = new ArrayList<>();
    public int size = 0;

    public ClassIRType(String name) {
        super();
        this.name = name;
    }

    public void addMember(BaseIRType member) {
        members.add(member);
        size += member.size();
    }

    public int getOffset(int idx) {
        int offset = 0;
        for (int i = 0; i < idx; ++i)
            offset += members.get(i).size();
        return offset;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(BaseIRType other) {
        return other instanceof ClassIRType && name.equals(((ClassIRType) other).name);
    }

    @Override
    public String toString() {
        return "%struct." + name;
    }
}
