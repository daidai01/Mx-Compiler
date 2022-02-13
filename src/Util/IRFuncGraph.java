package Util;

import IR.Instruction.BaseInst;
import IR.Instruction.FunCallInst;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;

import java.util.HashMap;
import java.util.HashSet;

public class IRFuncGraph {
    public IRRoot IRRoot;
    public boolean collectCallFunc;
    public HashMap<IRFunction, HashSet<IRFunction>> callFuncs = new HashMap<>();

    public IRFuncGraph(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
        collectCallFunc = true;
    }

    public void build() {
        if (collectCallFunc)
            IRRoot.funcs.forEach((name, func) -> callFuncs.put(func, new HashSet<>()));
        for (IRFunction func : IRRoot.funcs.values()) {
            func.callFuncs.clear();
            for (IRBlock block : func.blocks) {
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst) {
                    if (inst instanceof FunCallInst && !IRRoot.builtinFuncs.containsKey(((FunCallInst) inst).func.name)) {
                        func.callFuncs.add(((FunCallInst) inst).func);
                        if (collectCallFunc)
                            callFuncs.get(((FunCallInst) inst).func).add(func);
                    }
                }
            }
        }
    }
}
