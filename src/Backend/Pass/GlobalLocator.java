package Backend.Pass;

import IR.Instruction.BaseInst;
import IR.Instruction.FunCallInst;
import IR.Instruction.LoadInst;
import IR.Instruction.StoreInst;
import IR.Operand.BaseOperand;
import IR.Operand.GlobalVarOperand;
import IR.Operand.Register;
import IR.Program.*;
import IR.Type.PointerIRType;
import Util.IRFuncGraph;

import java.util.*;

public class GlobalLocator implements Pass {
    public IRRoot IRRoot;
    public IRFuncGraph funcGraph;
    public HashMap<IRFunction, HashSet<GlobalVarOperand>> useMap = new HashMap<>();
    public HashMap<IRFunction, HashSet<GlobalVarOperand>> defMap = new HashMap<>();
    public HashMap<IRFunction, HashSet<IRFunction>> funCallMap = new HashMap<>();
    public HashSet<IRFunction> visits = new HashSet<>();
    public ArrayList<IRFunction> dfsFuncs = new ArrayList<>();
    public HashSet<IRFunction> noInlineFuncs = new HashSet<>();

    public GlobalLocator(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
        funcGraph = new IRFuncGraph(IRRoot);
        funcGraph.build();
    }

    @Override
    public void run() {
        collect();
        for (IRFunction func : IRRoot.funcs.values())
            if (!visits.contains(func)) dfs(func);

        for (IRFunction func : IRRoot.funcs.values()) {
            if (noInlineFuncs.contains(func)) continue;
            HashSet<GlobalVarOperand> gVars = new HashSet<>(useMap.get(func));
            funCallMap.get(func).forEach(callFunc -> gVars.removeAll(useMap.get(callFunc)));
            HashMap<BaseOperand, Register> localMap = new HashMap<>();
            for (GlobalVarOperand gVar : gVars) {
                Register register = new Register(gVar.type, "local_" + gVar.name);
                localMap.put(gVar, register);
                func.vars.add(register);
            }
            for (IRBlock block : func.blocks) {
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                    HashSet<BaseOperand> uses = inst.getUses();
                    uses.retainAll(gVars);
                    if (!uses.isEmpty()) {
                        if (inst instanceof LoadInst && uses.contains(((LoadInst) inst).address))
                            ((LoadInst) inst).address = (localMap.get(((LoadInst) inst).address));
                        else if (inst instanceof StoreInst && uses.contains(((StoreInst) inst).address))
                            ((StoreInst) inst).address = (localMap.get(((StoreInst) inst).address));
                        else if (!(inst instanceof LoadInst) && !(inst instanceof StoreInst))
                            throw new RuntimeException("not load/store instruction");
                    }
                }
                IRBlock entryBlock = func.entryBlock;
                IRBlock exitBlock = func.exitBlock;
                for (Map.Entry<BaseOperand, Register> entry : localMap.entrySet()) {
                    BaseOperand operand = entry.getKey();
                    Register register = entry.getValue();
                    Register headReg = new Register(((PointerIRType) operand.type).basicType, "tmp_load_" + ((GlobalVarOperand) operand).name);
                    entryBlock.addHeadInst(new StoreInst(entryBlock, register, headReg));
                    entryBlock.addHeadInst(new LoadInst(entryBlock, headReg, operand));
                    if (defMap.get(func).contains(operand)) {
                        Register tailReg = new Register(((PointerIRType) operand.type).basicType, "tmp_load_" + ((GlobalVarOperand) operand).name);
                        exitBlock.addInstToTerminated(new LoadInst(exitBlock, tailReg, register));
                        exitBlock.addInstToTerminated(new StoreInst(exitBlock, operand, tailReg));
                    }
                }
            }
        }
    }

    private void collect() {
        Queue<IRFunction> funcQueue = new LinkedList<>();
        HashSet<IRFunction> inQueueFuncs = new HashSet<>();
        for (IRFunction func : IRRoot.funcs.values()) {
            HashSet<GlobalVarOperand> uses = new HashSet<>();
            HashSet<GlobalVarOperand> allDefs = new HashSet<>();
            HashSet<IRFunction> calls = new HashSet<>();
            for (IRBlock block : func.blocks) {
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                    for (BaseOperand use : inst.getUses()) {
                        if (use instanceof GlobalVarOperand) {
                            uses.add((GlobalVarOperand) use);
                            if (inst instanceof StoreInst) allDefs.add((GlobalVarOperand) use);
                        }
                    }
                    if (inst instanceof FunCallInst) {
                        IRFunction callFunc = ((FunCallInst) inst).func;
                        if (!IRRoot.builtinFuncs.containsKey(func.name))
                            calls.add(callFunc);
                    }
                }
            }
            this.useMap.put(func, uses);
            defMap.put(func, allDefs);
            funCallMap.put(func, calls);
            funcQueue.offer(func);
            inQueueFuncs.add(func);
        }
        while (!funcQueue.isEmpty()) {
            IRFunction func = funcQueue.poll();
            inQueueFuncs.remove(func);
            HashSet<GlobalVarOperand> funcUses = useMap.get(func);
            for (IRFunction callFunc : funcGraph.callFuncs.get(func)) {
                useMap.get(callFunc).addAll(funcUses);
                if (!inQueueFuncs.contains(callFunc)) {
                    inQueueFuncs.add(callFunc);
                    funcQueue.offer(callFunc);
                }
            }
        }
    }

    private void dfs(IRFunction func) {
        visits.add(func);
        dfsFuncs.add(func);
        for (IRFunction dfsFunc : dfsFuncs)
            if (func.callFuncs.contains(dfsFunc))
                noInlineFuncs.add(dfsFunc);
        for (IRFunction callFunc : funCallMap.get(func)) {
            if (!visits.contains(callFunc)) dfs(callFunc);
            funcGraph.callFuncs.get(callFunc).add(func);
        }
        dfsFuncs.remove(dfsFuncs.size() - 1);
    }
}
