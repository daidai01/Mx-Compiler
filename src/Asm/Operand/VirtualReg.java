package Asm.Operand;

public class VirtualReg extends Register {
    public int size;

    public VirtualReg(int name, int size) {
        super(name + "%");
        this.size = size;
    }

    @Override
    public String toString() {
        if (color == null) return name;
        else return color.toString();
    }
}
