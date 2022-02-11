package Asm.Instruction;

import Asm.Operand.Register;
import Asm.Program.AsmBlock;
import Asm.Program.AsmRoot;

import java.util.HashSet;

public class RetInst extends BaseInst {
    public AsmRoot asmRoot;

    public RetInst(AsmBlock block, AsmRoot asmRoot) {
        super(block, null);
        this.asmRoot = asmRoot;
    }

    @Override
    public HashSet<Register> uses() {
        HashSet<Register> uses=new HashSet<>();
        uses.add(asmRoot.physicalRegs.get(1));
        return uses;
    }

    @Override
    public String toString() {
        return "ret";
    }
}
