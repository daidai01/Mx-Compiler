package Asm.Operand;

public class Register extends BaseOperand {
    public String name;
    public PhysicalReg color = null;

    public Register(String name) {
        this.name = name;
        if (this instanceof PhysicalReg) color = (PhysicalReg) this;
    }

    @Override
    public String toString() {
        return name;
    }
}
