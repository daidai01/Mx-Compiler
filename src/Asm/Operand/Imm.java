package Asm.Operand;

public class Imm extends BaseOperand {
    public int value;
    public boolean canReverse = false;
    public boolean reverse = false;

    public Imm(int value) {
        this.value = value;
    }

    public Imm(int value, boolean reverse) {
        this.value = value;
        this.canReverse = true;
        this.reverse = reverse;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
