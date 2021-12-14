package IR.Instruction;

import IR.Program.Block;

abstract public class BaseInst {
    public Block block;
    public BaseInst preInst;
    public BaseInst nxtInst;

    public BaseInst(Block block){
        this.block=block;
    }


}
