package Util;

import IR.Program.IRBlock;
import IR.Program.IRFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class DomGenerator { //dominator tree
    public IRFunction func;
    public int counter = 0;
    public ArrayList<ArrayList<IRBlock>> bucket = new ArrayList<>();
    public HashMap<IRBlock, Integer> dfsOrder = new HashMap<>();
    public ArrayList<IRBlock> dfsIndex = new ArrayList<>();
    public HashMap<IRBlock, IRBlock> parents = new HashMap<>();
    public HashMap<IRBlock, LinkedList<IRBlock>> tree = new HashMap<>();
    public HashMap<IRBlock, IRBlock> minVertices = new HashMap<>();
    public HashMap<IRBlock, IRBlock> union = new HashMap<>();
    public HashMap<IRBlock, IRBlock> domBlocks = new HashMap<>();
    public int num = 0;

    public DomGenerator(IRFunction func) {
        this.func = func;
    }

    public void visitFunc() {
        dfsIndex.add(null);
        dfs(func.entryBlock);
        parents.put(func.entryBlock, null);

        for (int i = 0; i < counter; ++i)
            bucket.add(new ArrayList<>());
        for (int i = counter; i > 1; --i) {
            IRBlock tmpBlock = dfsIndex.get(i);
            for (IRBlock pressessor : tmpBlock.pressessors) {
                IRBlock evalBlock = eval(pressessor);
                if (dfsOrder.get(domBlocks.get(tmpBlock)) > dfsOrder.get(domBlocks.get(evalBlock)))
                    domBlocks.put(tmpBlock, domBlocks.get(evalBlock));
            }
            bucket.get(dfsOrder.get(domBlocks.get(tmpBlock))).add(tmpBlock);
            IRBlock parent = parents.get(tmpBlock);
            for (IRBlock block : bucket.get(dfsOrder.get(parent))) {
                IRBlock evalBlock = eval(block);
                block.domBlock = dfsOrder.get(domBlocks.get(block)) < dfsOrder.get(parent) ? evalBlock : parent;
            }
            bucket.get(dfsOrder.get(parent)).clear();
        }
        for (int i = 2; i <= counter; ++i) {
            IRBlock tmpBlock = dfsIndex.get(i);
            if (tmpBlock.domBlock != domBlocks.get(tmpBlock))
                tmpBlock.domBlock = tmpBlock.domBlock.domBlock;
        }
        for (int i = 1; i < dfsIndex.size(); ++i) {
            IRBlock tmpBlock = dfsIndex.get(i);
            if (tmpBlock.pressessors.size() <= 1) continue;
            for (IRBlock pressessor : tmpBlock.pressessors)
                while (pressessor != tmpBlock.domBlock) {
                    pressessor.domFrontiers.add(tmpBlock);
                    pressessor = pressessor.domBlock;
                }
        }
        func.blocks.forEach(block -> tree.put(block, new LinkedList<>()));
        for (IRBlock block : func.blocks) {
            if (block.domBlock != null)
                tree.get(block.domBlock).add(block);
            block.domEntry = block.domExit = -1;
        }
        dfsTree(func.entryBlock);
    }

    private void dfs(IRBlock block) {
        if (dfsOrder.containsKey(block)) return;
        block.domBlock = null;
        block.domEntry = -1;
        block.domExit = -1;
        block.domFrontiers.clear();
        dfsOrder.put(block, ++counter);
        dfsIndex.add(block);
        minVertices.put(block, block);
        union.put(block, block);
        domBlocks.put(block, block);
        for (IRBlock successor : block.successors)
            if (!dfsOrder.containsKey(successor)) {
                dfs(successor);
                parents.put(successor, block);
            }
    }

    private void dfsTree(IRBlock block) {
        block.domEntry = num++;
        tree.get(block).forEach(blk -> dfsTree(blk));
        block.domExit = num++;
    }

    private IRBlock eval(IRBlock block) {
        if (union.get(block) != union.get(union.get(block))) {
            if (dfsOrder.get(domBlocks.get(minVertices.get(block))) > dfsOrder.get(domBlocks.get(eval(union.get(block)))))
                minVertices.put(block, eval(union.get(block)));
            union.put(block, union.get(union.get(block)));
        }
        return minVertices.get(block);
    }
}
