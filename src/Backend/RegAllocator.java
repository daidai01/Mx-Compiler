package Backend;

import Asm.Instruction.*;
import Asm.Operand.Imm;
import Asm.Operand.PhysicalReg;
import Asm.Operand.Register;
import Asm.Operand.VirtualReg;
import Asm.Program.AsmBlock;
import Asm.Program.AsmFunction;
import Asm.Program.AsmRoot;
import Backend.Pass.Pass;

import java.util.*;

public class RegAllocator implements Pass {
    private class Edge {
        public Register u;
        public Register v;

        public Edge(Register u, Register v) {
            this.u = u;
            this.v = v;
        }

        public boolean equals(Edge other) {
            return other.u == u && other.v == v;
        }
    }

    public HashSet<Register> precolored = new HashSet<>();
    public HashSet<Register> initial = new LinkedHashSet<>();
    public HashSet<Register> simplifyWorkList = new LinkedHashSet<>();
    public HashSet<Register> freezeWorkList = new HashSet<>();
    public HashSet<Register> spillWorkList = new LinkedHashSet<>();
    public HashSet<Register> spilledNodes = new LinkedHashSet<>();
    public HashSet<Register> coalescedNodes = new LinkedHashSet<>();
    public HashSet<Register> coloredNodes = new HashSet<>();
    public Stack<Register> selectStack = new Stack<>();

    public HashSet<MvInst> coalescedMoves = new HashSet<>();
    public HashSet<MvInst> constrainedMoves = new HashSet<>();
    public HashSet<MvInst> frozenMoves = new HashSet<>();
    public HashSet<MvInst> workListMoves = new LinkedHashSet<>();
    public HashSet<MvInst> activeMoves = new HashSet<>();

    public AsmRoot asmRoot;
    public AsmFunction currentFunc = null;
    public HashSet<Edge> adjSet = new HashSet<>();
    public int K = 0;
    public int stackSize = 0;

    public RegAllocator(AsmRoot asmRoot) {
        this.asmRoot = asmRoot;
    }

    @Override
    public void run() {
        for (AsmFunction func : asmRoot.funcs) {
            currentFunc = func;
            stackSize = 0;
            boolean flag = true;
            while (flag) {
                init();
                new LivenessAnalysis(func).visitFunc();
                build();
                makeWorkList();
                while (!simplifyWorkList.isEmpty() || !workListMoves.isEmpty() || !freezeWorkList.isEmpty() || !spillWorkList.isEmpty()) {
                    if (!simplifyWorkList.isEmpty()) simplify();
                    else if (!workListMoves.isEmpty()) coalesce();
                    else if (!freezeWorkList.isEmpty()) freeze();
                    else if (!spillWorkList.isEmpty()) selectSpill();
                }
                assignColors();
                if (spilledNodes.isEmpty()) {
                    rewriteProgram();
                    flag = true;
                } else flag = false;
            }

            for (AsmBlock block : currentFunc.blocks)
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
                    inst.updateImm(stackSize);
            for (AsmBlock block : currentFunc.blocks)
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
                    if (inst instanceof MvInst && ((MvInst) inst).origin.color == inst.register.color)
                        inst.remove();
            HashSet<AsmBlock> mixBlocks = new HashSet<>();
            for (AsmBlock block : currentFunc.blocks)
                if (block.headInst instanceof JpInst)
                    mixBlocks.add(block);
            for (AsmBlock block : mixBlocks) {
                AsmBlock successor = ((JpInst) block.headInst).targetBlock;
                while (mixBlocks.contains(successor))
                    successor = ((JpInst) successor.headInst).targetBlock;
                for (AsmBlock pressessor : block.pressessors) {
                    for (BaseInst inst = pressessor.headInst; inst != null; inst = inst.nxtInst)
                        if (inst instanceof BrInst || inst instanceof BzInst || inst instanceof JpInst)
                            inst.replaceTargetBlock(block, successor);
                    pressessor.successors.remove(block);
                    pressessor.successors.add(successor);
                }
                successor.pressessors.remove(block);
                successor.pressessors.addAll(block.pressessors);
                if (block == currentFunc.entryBlock) currentFunc.entryBlock = successor;
            }
            currentFunc.blocks.removeAll(mixBlocks);

            Queue<AsmBlock> blockQueue = new LinkedList<>();
            blockQueue.add(currentFunc.entryBlock);
            HashSet<AsmBlock> visits = new HashSet<>();
            visits.add(currentFunc.entryBlock);
            while (!blockQueue.isEmpty()) {
                AsmBlock currentBlock = blockQueue.poll();
                if (currentBlock.tailInst instanceof JpInst) {
                    JpInst jpInst = (JpInst) currentBlock.tailInst;
                    if (!jpInst.targetBlock.hasPreBlock) {
                        currentBlock.nxtBlock = jpInst.targetBlock;
                        currentBlock.tailInst = jpInst.preInst;
                        currentBlock.tailInst.nxtInst = null;
                        jpInst.preInst = null;
                        jpInst.targetBlock.hasPreBlock = true;
                    }
                }
                for (AsmBlock successor : currentBlock.successors)
                    if (!visits.contains(successor)) blockQueue.offer(successor);
                visits.addAll(currentBlock.successors);
            }
        }
    }

    private void init() {
        initial.clear();
        simplifyWorkList.clear();
        freezeWorkList.clear();
        spillWorkList.clear();
        spilledNodes.clear();
        coalescedNodes.clear();
        coloredNodes.clear();
        selectStack.clear();
        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        workListMoves.clear();
        activeMoves.clear();
        adjSet.clear();
        for (AsmBlock block : currentFunc.blocks)
            for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                initial.addAll(inst.getDefs());
                initial.addAll(inst.getUses());
            }
        initial.removeAll(precolored);
        for (Register register : initial) {
            register.moveList.clear();
            register.adjList.clear();
            register.degree = 0;
            register.alias = null;
            register.color = null;
            register.weight = 0;
        }
        for (Register register : precolored) {
            register.moveList.clear();
            register.adjList.clear();
            register.degree = 1147483640;
            register.alias = null;
            register.color = (PhysicalReg) register;
        }
        for (AsmBlock block : currentFunc.blocks) {
            double weight = Math.pow(10, block.loopDepth);
            for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                inst.getUses().forEach(register -> register.weight += weight);
                if (inst.register != null) inst.register.weight += weight;
            }
        }
    }

    private void build() {
        for (AsmBlock block : currentFunc.blocks) {
            HashSet<Register> live = new HashSet<>(block.liveOuts);
            for (BaseInst inst = block.tailInst; inst != null; inst = inst.preInst) {
                if (inst instanceof MvInst) {
                    live.removeAll(inst.getUses());
                    HashSet<Register> regs = new HashSet<>();
                    regs.addAll(inst.getDefs());
                    regs.addAll(inst.getUses());
                    for (Register reg : regs)
                        reg.moveList.add((MvInst) inst);
                    workListMoves.add((MvInst) inst);
                }
                live.add(asmRoot.physicalRegs.get(0));
                live.addAll(inst.getDefs());
                for (Register def : inst.getDefs())
                    for (Register lv : live)
                        addEdge(lv, def);
                live.removeAll(inst.getDefs());
                live.addAll(inst.getUses());
            }
        }
    }

    private void addEdge(Register u, Register v) {
        if (adjSet.contains(new Edge(u, v)) || u == v) return;
        adjSet.add(new Edge(u, v));
        adjSet.add(new Edge(v, u));
        if (!precolored.contains(u)) {
            u.adjList.add(v);
            u.degree++;
        }
        if (!precolored.contains(v)) {
            v.adjList.add(u);
            v.degree++;
        }
    }

    private void makeWorkList() {
        for (Register n : initial) {
            if (n.degree >= K) spillWorkList.add(n);
            else if (moveRelated(n)) freezeWorkList.add(n);
            else simplifyWorkList.add(n);
        }
    }

    private HashSet<Register> adjacent(Register n) {
        HashSet<Register> regs = new HashSet<>(n.adjList);
        regs.removeAll(selectStack);
        regs.removeAll(coalescedNodes);
        return regs;
    }

    private HashSet<MvInst> nodeMoves(Register n) {
        HashSet<MvInst> mvInsts = new HashSet<>(activeMoves);
        mvInsts.addAll(workListMoves);
        mvInsts.retainAll(n.moveList);
        return mvInsts;
    }

    private boolean moveRelated(Register n) {
        return !nodeMoves(n).isEmpty();
    }

    private void simplify() {
        Register n = simplifyWorkList.iterator().next();
        simplifyWorkList.remove(n);
        selectStack.push(n);
        for (Register m : adjacent(n))
            decrementDegree(m);
    }

    private void decrementDegree(Register m) {
        int d = m.degree;
        m.degree--;
        if (d == K) {
            HashSet<Register> regs = new HashSet<>();
            regs.add(m);
            regs.addAll(adjacent(m));
            enableMoves(regs);
            spillWorkList.remove(m);
            if (moveRelated(m)) freezeWorkList.remove(m);
            else simplifyWorkList.add(m);
        }
    }

    private void enableMoves(HashSet<Register> nodes) {
        for (Register n : nodes)
            for (MvInst m : nodeMoves(n))
                if (activeMoves.contains(m)) {
                    activeMoves.remove(m);
                    workListMoves.add(m);
                }
    }

    private void coalesce() {
        MvInst m = workListMoves.iterator().next();
        Register x = getAlias(m.register);
        Register y = getAlias(m.origin);
        Register u;
        Register v;
        if (precolored.contains(y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        workListMoves.remove(m);
        if (u == v) {
            coalescedMoves.add(m);
            addWorkList(u);
        } else if (precolored.contains(v) || adjSet.contains(new Edge(u, v))) {
            constrainedMoves.add(m);
            addWorkList(u);
            addWorkList(v);
        } else {
            boolean flag = true;
            for (Register t : adjacent(v))
                if (!ok(t, u)) {
                    flag = false;
                    break;
                }
            HashSet<Register> regs = new HashSet<>(adjacent(u));
            regs.addAll(adjacent(v));
            if (precolored.contains(u) && flag || (!precolored.contains(u) && conservative(regs))) {
                coalescedMoves.add(m);
                combine(u, v);
                addWorkList(u);
            } else activeMoves.add(m);
        }
    }

    private void addWorkList(Register u) {
        if (!precolored.contains(u) && !moveRelated(u) && u.degree < K) {
            freezeWorkList.remove(u);
            simplifyWorkList.add(u);
        }
    }

    private boolean ok(Register t, Register r) {
        return t.degree < K || precolored.contains(t) || adjSet.contains(new Edge(t, r));
    }

    private boolean conservative(HashSet<Register> nodes) {
        int k = 0;
        for (Register n : nodes)
            if (n.degree >= K) k++;
        return k < K;
    }

    private Register getAlias(Register n) {
        if (coalescedNodes.contains(n)) {
            n.alias = getAlias(n.alias);
            return n.alias;
        } else return n;
    }

    private void combine(Register u, Register v) {
        if (freezeWorkList.contains(v)) freezeWorkList.remove(v);
        else spillWorkList.remove(v);
        coalescedNodes.add(v);
        v.alias = u;
        u.moveList.addAll(v.moveList);
        HashSet<Register> vSet = new HashSet<>();
        vSet.add(v);
        enableMoves(vSet);
        for (Register t : adjacent(v)) {
            addEdge(t, u);
            decrementDegree(t);
        }
        if (u.degree >= K && freezeWorkList.contains(u)) {
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    private void freeze() {
        Register u = freezeWorkList.iterator().next();
        freezeWorkList.remove(u);
        simplifyWorkList.add(u);
        freezeMoves(u);
    }

    private void freezeMoves(Register u) {
        for (MvInst m : nodeMoves(u)) {
            Register x = m.register;
            Register y = m.origin;
            Register v;
            if (getAlias(y) == getAlias(u)) v = getAlias(x);
            else v = getAlias(y);
            activeMoves.remove(m);
            frozenMoves.add(m);
            if (nodeMoves(v).isEmpty() && v.degree < K) {
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }

    private void selectSpill() {
        Register m = spillWorkList.iterator().next(); //todo
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }

    private void assignColors() {
        while (!selectStack.isEmpty()) {
            Register n = selectStack.pop();
            ArrayList<PhysicalReg> okColors = new ArrayList<>(asmRoot.kRegs);
            HashSet<Register> regs = new HashSet<>(coloredNodes);
            regs.addAll(precolored);
            for (Register w : n.adjList)
                if (regs.contains(getAlias(w)))
                    okColors.remove(getAlias(w).color);
            if (okColors.isEmpty()) spilledNodes.add(n);
            else {
                coloredNodes.add(n);
                n.color = okColors.get(0);
            }
        }
        for (Register n : coalescedNodes)
            n.color = getAlias(n).color;
    }

    private void rewriteProgram() {
        for (Register v : spilledNodes) {
            v.offset = new Imm(-1 * stackSize - 4, false);
            stackSize += 4;
        }
        for (AsmBlock block : currentFunc.blocks)
            for (BaseInst inst = block.headInst; inst != null; inst = inst.preInst.nxtInst)
                if (inst.register != null && inst.register instanceof VirtualReg)
                    getAlias(inst.register);
        for (AsmBlock block : currentFunc.blocks)
            for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                for (Register use : inst.getUses()) {
                    if (use.offset == null) continue;
                    if (inst.getDefs().contains(use)) {
                        VirtualReg virtualReg = new VirtualReg(++currentFunc.counter, ((VirtualReg) use).size);
                        inst.replaceUse(use, virtualReg);
                        if (use == inst.register) inst.register = virtualReg;
                        inst.addPreInst(new LdInst(block, virtualReg, asmRoot.physicalRegs.get(2), use.offset, virtualReg.size));
                        inst.addNxtInst(new StInst(block, asmRoot.physicalRegs.get(2), virtualReg, use.offset, virtualReg.size));
                    } else if (inst instanceof MvInst && inst.register.offset == null && ((MvInst) inst).origin == use) {
                        BaseInst replacer = new LdInst(block, inst.register, asmRoot.physicalRegs.get(2), use.offset, ((VirtualReg) use).size);
                        inst.replaceInst(replacer);
                        inst = replacer;
                    } else {
                        VirtualReg virtualReg = new VirtualReg(++currentFunc.counter, ((VirtualReg) use).size);
                        inst.addPreInst(new LdInst(block, virtualReg, asmRoot.physicalRegs.get(2), use.offset, virtualReg.size));
                        inst.replaceUse(use, virtualReg);
                    }
                }
                for (Register def : inst.getDefs()) {
                    if (def.offset == null || inst.getUses().contains(def)) continue;
                    if (inst instanceof MvInst && ((MvInst) inst).origin.offset == null) {
                        BaseInst replacer = new StInst(block, asmRoot.physicalRegs.get(2), ((MvInst) inst).origin, def.offset, ((VirtualReg) def).size);
                        inst.replaceInst(replacer);
                        inst = replacer;
                    } else {
                        VirtualReg virtualReg = new VirtualReg(++currentFunc.counter, ((VirtualReg) def).size);
                        if (def == inst.register) inst.register = virtualReg;
                        inst.addNxtInst(new StInst(block, asmRoot.physicalRegs.get(2), virtualReg, def.offset, ((VirtualReg) def).size));
                    }
                }
            }
    }
}
