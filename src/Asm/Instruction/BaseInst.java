package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;

import java.util.HashSet;

abstract public class BaseInst {
    public AsmBlock block;
    public Register register;
    public BaseInst preInst=null;
    public BaseInst nxtInst=null;

    public BaseInst(AsmBlock block, Register register){
        this.block=block;
        this.register=register;
    }

    abstract public HashSet<Register> uses();

    abstract public String toString();
}
