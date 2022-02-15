package Asm.Operand;

import Asm.Instruction.MvInst;

import java.util.HashSet;

public class Register extends BaseOperand {
    public String name;
    public HashSet<MvInst> moveList = new HashSet<>();
    public HashSet<Register> adjList = new HashSet<>();
    public int degree = 0;
    public Register alias = null;
    public PhysicalReg color = null;
    public Imm offset = null;
    public int weight = 0;

    public Register(String name) {
        this.name = name;
        if (this instanceof PhysicalReg) color = (PhysicalReg) this;
    }

    @Override
    public String toString() {
        return name;
    }
}
