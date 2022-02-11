package Backend.Pass;

import IR.Instruction.*;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;

import java.util.*;

public class PhiResolver implements Pass {
    public IRRoot IRRoot;

    public PhiResolver(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
    }

    @Override
    public void run() {
        for (IRFunction func : IRRoot.funcs.values()) {
            HashSet<NeighborBlock> neighborBlocks = new HashSet<>();
            for (IRBlock block : func.blocks)
                if (block.successors.size() > 1)
                    for (IRBlock successor : block.successors)
                        if (successor.successors.size() > 1)
                            neighborBlocks.add(new NeighborBlock(block, successor));
            for (NeighborBlock neighborBlock : neighborBlocks) {
                IRBlock midBlock = new IRBlock("mid_block");
                func.blocks.add(midBlock);
                midBlock.addTerminalInst(new JumpInst(midBlock, neighborBlock.sucBlock));
                for (PhiInst inst : neighborBlock.sucBlock.phis.values())
                    for (int i = 0; i < inst.blocks.size(); ++i)
                        if (inst.blocks.get(i) == neighborBlock.preBlock)
                            inst.blocks.set(i, midBlock);
                neighborBlock.preBlock.replaceSuccessor(neighborBlock.sucBlock, midBlock);
            }
            HashMap<IRBlock, Copy> copies = new HashMap<>();
            func.blocks.forEach(block -> copies.put(block, new Copy()));
            for (IRBlock block : func.blocks)
                for (Map.Entry<Register, PhiInst> entry : block.phis.entrySet()) {
                    PhiInst inst = entry.getValue();
                    for (int i = 0; i < inst.blocks.size(); ++i) {
                        IRBlock preBlock = inst.blocks.get(i);
                        BaseOperand value = inst.values.get(i);
                        copies.get(preBlock).addMoveInst(new MoveInst(preBlock, entry.getKey(), value, false));
                    }
                }
            copies.forEach(this::visitBlock);
            HashSet<IRBlock> mixBlocks = new HashSet<>();
            for (IRBlock block : func.blocks)
                if (block.headInst instanceof JumpInst)
                    mixBlocks.add(block);
            for (IRBlock block : mixBlocks) {
                IRBlock sucBlock = ((JumpInst) block.tailInst).targetBlock;
                while (mixBlocks.contains(sucBlock))
                    sucBlock = ((JumpInst) block.tailInst).targetBlock;
                for (IRBlock pressessor : new HashSet<>(block.pressessors))
                    pressessor.replaceSuccessor(block, sucBlock);
                if (block == func.entryBlock) func.entryBlock = sucBlock;
            }
            func.blocks.removeAll(mixBlocks);
        }
    }

    private class NeighborBlock {
        public IRBlock preBlock;
        public IRBlock sucBlock;

        public NeighborBlock(IRBlock preBlock, IRBlock sucBlock) {
            this.preBlock = preBlock;
            this.sucBlock = sucBlock;
        }
    }

    private class Copy {
        public ArrayList<MoveInst> moveInsts = new ArrayList<>();
        public HashMap<BaseOperand, Integer> uses = new HashMap<>();

        public void addMoveInst(MoveInst inst) {
            moveInsts.add(inst);
            BaseOperand srcOperand = inst.src;
            if (srcOperand instanceof Register) {
                if (uses.containsKey(srcOperand)) uses.put(srcOperand, uses.get(srcOperand) + 1);
                else uses.put(srcOperand, 1);
            }
        }
    }

    private void visitBlock(IRBlock block, Copy copy) {
        boolean flag = true;
        while (flag) {
            boolean hasMore = false;
            for (Iterator<MoveInst> iter = copy.moveInsts.iterator(); iter.hasNext(); ) {
                MoveInst nxtMove = iter.next();
                if (!copy.uses.containsKey(nxtMove.register)) {
                    iter.remove();
                    if (nxtMove.src instanceof Register) {
                        int idx = copy.uses.get(nxtMove.src) - 1;
                        if (idx > 0) copy.uses.put(nxtMove.src, idx);
                        else copy.uses.remove(nxtMove.src);
                    }
                    block.addTerminalInst(new MoveInst(block, nxtMove.register, nxtMove.src, true));
                    hasMore = true;
                }
            }
            if (!hasMore)
                for (int i = 0; i < copy.moveInsts.size(); ++i) {
                    MoveInst inst = copy.moveInsts.get(i);
                    if (inst.src == inst.register) continue;
                    Register mirrorReg = new Register(inst.src.type, "mirror_" + inst.src);
                    block.addTerminalInst(new MoveInst(block, mirrorReg, inst.src, true));
                    copy.uses.remove(inst.src);
                    copy.moveInsts.forEach(moveInst -> moveInst.replaceSrc(inst.src, mirrorReg));
                    break;
                }
            flag = false;
            for (int i = 0; i < copy.moveInsts.size(); ++i) {
                MoveInst inst = copy.moveInsts.get(i);
                if (inst.src == inst.register) continue;
                flag = true;
                break;
            }
        }
    }
}
