package IR.Program;

import IR.Instruction.*;
import IR.Operand.Register;
import Util.Error.InternalError;
import Util.Position;

import java.util.ArrayList;
import java.util.HashMap;

public class IRBlock {
    public String name;
    public ArrayList<IRBlock> successors = new ArrayList<>();
    public ArrayList<IRBlock> pressessors = new ArrayList<>();
    public BaseInst headInst = null;
    public BaseInst tailInst = null;
    public int loopDepth = 0;
    public boolean terminated = false;
    public HashMap<Register, PhiInst> phis = new HashMap<>();
    public boolean isDom = false;

    public IRBlock(String name) {
        this.name = name;
    }

    public void addInst(BaseInst inst) {
        if (headInst == null) headInst = inst;
        else {
            tailInst.nxtInst = inst;
            inst.preInst = tailInst;
        }
        tailInst = inst;
    }

    public void removeInst(BaseInst inst) {
        if (inst instanceof PhiInst) phis.remove(inst.register);
        else if (inst instanceof ReturnInst || inst instanceof JumpInst || inst instanceof BranchInst)
            removeTerminalInst();
        else inst.removeInst();
    }

    public void addTerminalInst(BaseInst inst) {
        addInst(inst);
        terminated = true;
        if (inst instanceof JumpInst) {
            IRBlock targetBlock = ((JumpInst) inst).targetBlock;
            successors.add(targetBlock);
            targetBlock.pressessors.add(this);
        } else if (inst instanceof BranchInst) {
            IRBlock targetBlock = ((BranchInst) inst).trueBranch;
            successors.add(targetBlock);
            targetBlock.pressessors.add(this);
            targetBlock = ((BranchInst) inst).falseBranch;
            successors.add(targetBlock);
            targetBlock.pressessors.add(this);
        }
    }

    public boolean retTerminated() {
        return terminated && tailInst instanceof ReturnInst;
    }

    public void removeTerminalInst() {
        if (!terminated) return;
        terminated = false;
        if (tailInst instanceof JumpInst) removeSuccessor(((JumpInst) tailInst).targetBlock);
        else if (tailInst instanceof BranchInst) {
            removeSuccessor(((BranchInst) tailInst).trueBranch);
            removeSuccessor(((BranchInst) tailInst).falseBranch);
        }
        tailInst.removeInst();
        tailInst.remove(true);
    }

    public void addInstToTerminated(BaseInst inst) {
        if (tailInst.preInst == null) {
            inst.preInst = null;
            inst.nxtInst = tailInst;
            tailInst.preInst = inst;
            headInst = inst;
        } else {
            tailInst.preInst.nxtInst = inst;
            inst.preInst = tailInst.preInst;
            tailInst.preInst = inst;
            inst.nxtInst = tailInst;
        }
    }

    public void addPhi(PhiInst inst) {
        phis.put(inst.register, inst);
    }

    public void addHeadInst(BaseInst inst) {
        if (headInst == null) tailInst = inst;
        else {
            inst.nxtInst = headInst;
            headInst.preInst = inst;
        }
        headInst = inst;
    }

    public void removePressessor(IRBlock block) {
        pressessors.remove(block);
        phis.forEach(((register, inst) -> inst.removeBlock(block)));
    }

    public void removeSuccessor(IRBlock block) {
        block.removePressessor(this);
        successors.remove(block);
    }

    public void replaceSuccessor(IRBlock block1, IRBlock block2) {
        if (tailInst instanceof JumpInst) {
            removeTerminalInst();
            addTerminalInst(new JumpInst(this, block2));
        } else if (tailInst instanceof BranchInst) {
            BranchInst terminator = (BranchInst) tailInst;
            BranchInst newInst;
            if (terminator.trueBranch == block1)
                newInst = new BranchInst(this, terminator.condition, block2, terminator.falseBranch);
            else newInst = new BranchInst(this, terminator.condition, terminator.trueBranch, block2);
            removeTerminalInst();
            addTerminalInst(newInst);
        } else throw new InternalError("terminator type error", new Position(0, 0));
    }

    public boolean isDomed(IRBlock block){
        return false; //todo
    }
}
