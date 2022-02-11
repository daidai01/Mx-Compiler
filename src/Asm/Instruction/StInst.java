package Asm.Instruction;

import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

public class StInst extends BaseInst {
    public Register address;
    public Register value;
    public Imm offset;
    public int size;

    public StInst(AsmBlock block, Register register, Register address, Register value, Imm offset, int size) {
        super(block, register);
        this.address = address;
        this.value = value;
        this.offset = offset;
        this.size = size;
    }

    @Override
    public HashSet<Register> uses() {
        HashSet<Register> uses = new HashSet<>();
        uses.add(address);
        uses.add(value);
        return uses;
    }

    @Override
    public String toString() {
        String str;
        if (size == 1) str = "sb ";
        else if (size == 2) str = "sh ";
        else str = "sw ";
        return str + value + ", " + offset + "(" + address + ")";
    }
}
