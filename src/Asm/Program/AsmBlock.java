package Asm.Program;

import Asm.Instruction.BaseInst;
import Asm.Operand.Register;

import java.util.ArrayList;
import java.util.HashSet;

public class AsmBlock {
    public String name;
    public ArrayList<AsmBlock> successors = new ArrayList<>();
    public ArrayList<AsmBlock> pressessors = new ArrayList<>();
    public BaseInst headInst = null;
    public BaseInst tailInst = null;
    public int loopDepth = 0;
    public AsmBlock nxtBlock = null;
    public boolean hasPreBlock = false;

    public HashSet<Register> liveIns = new HashSet<>();
    public HashSet<Register> liveOuts = new HashSet<>();

    public AsmBlock(String name, int loopDepth) {
        this.name = name;
        this.loopDepth = loopDepth;
    }

    public void addInst(BaseInst inst) {
        if (headInst == null) headInst = inst;
        else {
            tailInst.nxtInst = inst;
            inst.preInst = tailInst;
        }
        tailInst = inst;
    }

}
