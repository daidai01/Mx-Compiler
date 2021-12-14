package IR.Program;

import IR.Instruction.BaseInst;

import java.util.LinkedHashSet;
import java.util.Set;

public class Block {
    public String name = null;
    public Function func = null;
    public Block preBlock = null;
    public Block nxtBlock = null;
    public Set<Block> successors = new LinkedHashSet<Block>();
    public Set<Block> pressessors = new LinkedHashSet<Block>();
    public BaseInst headInst = null;
    public BaseInst tailInst = null;
    public int loopDepth = 0;

    public Block(String name, Function func) {
        this.name = name;
        this.func = func;
    }
}
