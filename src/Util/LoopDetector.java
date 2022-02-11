package Util;

import IR.Program.IRBlock;
import IR.Program.IRFunction;

import java.util.*;

public class LoopDetector {
    public IRFunction func;
    public HashMap<IRBlock, Loop> loopMap = new HashMap<>();
    public HashSet<IRBlock> visitedBlocks = new HashSet<>();
    public HashSet<Loop> rootLoops = new HashSet<>();
    public Stack<Loop> loopStack = new Stack<>();

    private class Loop {
        public IRBlock headBlock;
        public HashSet<IRBlock> blocks = new HashSet<>();
        public HashSet<IRBlock> tailBlocks = new HashSet<>();
        public HashSet<Loop> children = new HashSet<>();
    }

    public LoopDetector(IRFunction func) {
        this.func = func;
    }

    public void visitFunc() {
        for (IRBlock block : func.blocks)
            for (IRBlock successor : block.successors)
                if (block.isDomed(successor)) {
                    if (!loopMap.containsKey(successor))
                        loopMap.put(successor, new Loop());
                    loopMap.get(successor).tailBlocks.add(block);
                    break;
                }
        for (Map.Entry<IRBlock, Loop> entry : loopMap.entrySet()) {
            IRBlock headBlock = entry.getKey();
            Loop loop = entry.getValue();
            for (IRBlock tailBlock : loop.tailBlocks)
                getWholeLoop(headBlock, tailBlock);
        }
        visitBlock(func.entryBlock);
    }

    private void getWholeLoop(IRBlock head, IRBlock tail) {
        HashSet<IRBlock> loopBlocks = new HashSet<>();
        loopBlocks.add(head);
        loopBlocks.add(tail);
        Queue<IRBlock> blockQueue = new LinkedList<>();
        blockQueue.offer(tail);
        while (!blockQueue.isEmpty()) {
            IRBlock block = blockQueue.poll();
            for (IRBlock pressessor : block.pressessors)
                if (!loopBlocks.contains(pressessor)) {
                    blockQueue.offer(pressessor);
                    loopBlocks.add(pressessor);
                }
        }
        loopMap.get(head).blocks.addAll(loopBlocks);
    }

    private void visitBlock(IRBlock block) {
        visitedBlocks.add(block);
        while (!loopStack.isEmpty() && !loopStack.peek().blocks.contains(block))
            loopStack.pop();
        if (loopMap.containsKey(block)) {
            Loop loop = loopMap.get(block);
            if (loopStack.isEmpty()) rootLoops.add(loop);
            else loopStack.peek().children.add(loop);
            loopStack.push(loop);
        }
        block.loopDepth = loopStack.size();
        for (IRBlock successor : block.successors)
            if (!visitedBlocks.contains(successor))
                visitBlock(successor);
    }
}
