package Asm.Operand;

public class GlobalReg extends Register {
    public int size;

    public GlobalReg(String name, int size) {
        super(name);
        this.size = size;
    }
}
