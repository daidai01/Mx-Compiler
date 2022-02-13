package Backend;

import Asm.Instruction.BaseInst;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;
import Asm.Program.AsmFunction;

import java.util.*;

public class LivenessAnalysis {
    public AsmFunction func;
    public HashSet<AsmBlock> visits = new HashSet();
    public Queue<AsmBlock> blockQueue = new LinkedList<>();
    public HashMap<AsmBlock, HashSet<Register>> uses = new LinkedHashMap<>();
    public HashMap<AsmBlock, HashSet<Register>> defs = new LinkedHashMap<>();

    public LivenessAnalysis(AsmFunction func) {
        this.func = func;
    }

    public void visitFunc() {
        func.blocks.forEach(block -> visitBlock(block));
        blockQueue.offer(func.exitBlock);
        visits.add(func.exitBlock);
        while (!blockQueue.isEmpty()) {
            AsmBlock block = blockQueue.poll();
            visits.add(block);
            HashSet<Register> liveIns = new LinkedHashSet<>();
            HashSet<Register> liveOuts = new LinkedHashSet<>();
            block.successors.forEach(successor -> liveOuts.addAll(successor.liveIns));
            liveIns.addAll(liveOuts);
            liveIns.removeAll(defs.get(block));
            liveIns.addAll(uses.get(block));
            block.liveIns.addAll(liveIns);
            block.liveOuts.addAll(liveIns);
            if (!liveIns.isEmpty()) visits.removeAll(block.pressessors);
            for (AsmBlock pressessor : block.pressessors)
                if (!visits.contains(pressessor)) {
                    blockQueue.offer(pressessor);
                    visits.add(pressessor);
                }
        }
    }

    private void visitBlock(AsmBlock block) {
        HashSet<Register> usesForBlock = new LinkedHashSet<>();
        HashSet<Register> defsForBlock = new LinkedHashSet<>();
        for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
            HashSet<Register> useForInst = inst.getUses();
            useForInst.removeAll(defsForBlock);
            usesForBlock.addAll(useForInst);
            defsForBlock.addAll(inst.getDefs());
        }
        uses.put(block, usesForBlock);
        defs.put(block, defsForBlock);
        block.liveIns.clear();
        block.liveOuts.clear();
    }
}
