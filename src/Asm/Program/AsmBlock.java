package Asm.Program;

import Asm.Instruction.BaseInst;

import java.util.ArrayList;

public class AsmBlock {
    public String name;
    public ArrayList<AsmBlock> successors = new ArrayList<>();
    public ArrayList<AsmBlock> pressessors = new ArrayList<>();
    public BaseInst headInst = null;
    public BaseInst tailInst = null;
    public int loopDepth = 0;
    public AsmBlock nxtBlock = null;
    public boolean hasPreBlock = false;

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
