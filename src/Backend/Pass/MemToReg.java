package Backend.Pass;

import IR.Instruction.*;
import IR.Operand.BaseOperand;
import IR.Operand.Register;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;
import Util.DomGenerator;

import java.util.*;

public class MemToReg implements Pass {
    public IRRoot IRRoot;

    public MemToReg(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
    }

    @Override
    public void run() {
        for (IRFunction func : IRRoot.funcs.values()) {
            new DomGenerator(func).visitFunc();
            HashMap<IRBlock, HashSet<LoadInst>> loads = new LinkedHashMap<>();
            HashMap<IRBlock, HashMap<Register, BaseOperand>> stores = new LinkedHashMap<>();
            HashMap<IRBlock, HashMap<Register, PhiInst>> phis = new LinkedHashMap<>();
            HashSet<Register> vars = func.vars;
            HashSet<IRBlock> blocks = new LinkedHashSet<>();
            HashMap<BaseOperand, BaseOperand> replaceOperands = new LinkedHashMap<>();
            for (IRBlock block : func.blocks) {
                loads.put(block, new LinkedHashSet<>());
                stores.put(block, new LinkedHashMap<>());
                phis.put(block, new LinkedHashMap<>());
            }
            for (IRBlock block : func.blocks)
                for (BaseInst inst = block.headInst; inst != null; ) {
                    BaseInst nxtInst = inst.nxtInst;
                    if (inst instanceof LoadInst) {
                        BaseOperand address = ((LoadInst) inst).address;
                        if (address instanceof Register && vars.contains(address)) {
                            HashMap<Register, BaseOperand> liveouts = stores.get(inst.block);
                            if (liveouts.containsKey(address)) {
                                replaceOperands.put(inst.register, liveouts.get(address));
                                inst.remove(true);
                            } else loads.get(inst.block).add((LoadInst) inst);
                        }
                    } else if (inst instanceof StoreInst) {
                        BaseOperand address = ((StoreInst) inst).address;
                        if (address instanceof Register && vars.contains(address)) {
                            stores.get(inst.block).put((Register) address, ((StoreInst) inst).value);
                            blocks.add(inst.block);
                            inst.remove(true);
                        }
                    }
                    inst = nxtInst;
                }
            while (!blocks.isEmpty()) {
                HashSet<IRBlock> runningBlocks = blocks;
                blocks = new LinkedHashSet<>();
                for (IRBlock runningBlock : runningBlocks) {
                    if (stores.get(runningBlock).isEmpty()) continue;
                    for (IRBlock domFrontier : runningBlock.domFrontiers)
                        for (Map.Entry<Register, BaseOperand> entry : stores.get(runningBlock).entrySet()) {
                            if (phis.get(domFrontier).containsKey(entry.getKey())) continue;
                            Register register = new Register(entry.getValue().type, entry.getKey().name + "_phi");
                            PhiInst phiInst = new PhiInst(domFrontier, register, new ArrayList<>(), new ArrayList<>());
                            domFrontier.addPhi(phiInst);
                            if (!stores.get(domFrontier).containsKey(entry.getKey())) {
                                stores.get(domFrontier).put(entry.getKey(), register);
                                blocks.add(domFrontier);
                            }
                            phis.get(domFrontier).put(entry.getKey(), phiInst);
                        }
                }
            }
            for (IRBlock block : func.blocks) {
                if (!phis.get(block).isEmpty())
                    for (Map.Entry<Register, PhiInst> entry : phis.get(block).entrySet()) {
                        Register address = entry.getKey();
                        for (IRBlock pressessor : block.pressessors) {
                            IRBlock runningBlock = pressessor;
                            while (!stores.get(runningBlock).containsKey(address))
                                runningBlock = runningBlock.domBlock;
                            entry.getValue().add(pressessor, stores.get(runningBlock).get(address));
                        }
                    }
                if (!loads.get(block).isEmpty())
                    for (LoadInst loadInst : loads.get(block)) {
                        Register address = (Register) loadInst.address;
                        BaseOperand replacer = null;
                        if (phis.get(block).containsKey(address))
                            replacer = phis.get(block).get(address).register;
                        else {
                            IRBlock currentBlock = block.domBlock;
                            while (true) {
                                if (stores.get(currentBlock).containsKey(address)) {
                                    replacer = stores.get(currentBlock).get(address);
                                    break;
                                } else currentBlock = currentBlock.domBlock;
                            }
                        }
                        while (replaceOperands.containsKey(replacer))
                            replacer = replaceOperands.get(replacer);
                        replaceOperands.put(loadInst.register, replacer);
                        loadInst.remove(true);
                    }
            }
            for (Map.Entry<BaseOperand, BaseOperand> entry : replaceOperands.entrySet()) {
                BaseOperand replacer = entry.getValue();
                while (replaceOperands.containsKey(replacer))
                    replacer = replaceOperands.get(replacer);
                ((Register) entry.getKey()).replaceUse(replacer);
            }
            for (IRBlock block : func.blocks)
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
                    if (inst instanceof AllocaInst) inst.removeInst();
        }
    }
}
