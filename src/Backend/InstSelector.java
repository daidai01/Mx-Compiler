package Backend;

import Asm.Instruction.LiInst;
import Asm.Operand.GlobalReg;
import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Operand.VirtualReg;
import Asm.Program.AsmBlock;
import Asm.Program.AsmFunction;
import Asm.Program.AsmRoot;
import IR.Operand.*;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;
import IR.Type.PointerIRType;
import Util.LoopDetector;

import java.util.HashMap;
import java.util.Map;

public class InstSelector {
    public IRRoot IRRoot;
    public AsmRoot asmRoot = new AsmRoot();
    public AsmBlock currentBlock = null;
    public AsmFunction currentFunc = null;
    public HashMap<IRBlock, AsmBlock> blocks = new HashMap<>();
    public HashMap<IRFunction, AsmFunction> funcs = new HashMap<>();
    public HashMap<BaseOperand, Register> regs = new HashMap<>();
    public HashMap<Integer, Register> liRegs = new HashMap<>();
    public int counter = 0;

    public InstSelector(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
    }

    public AsmRoot run() {
        for (Map.Entry<String, IRFunction> entry : IRRoot.builtinFuncs.entrySet()) {
            AsmFunction func = new AsmFunction(entry.getKey());
            asmRoot.builtinFuncs.add(func);
            funcs.put(entry.getValue(), func);
        }
        for (Map.Entry<String, IRFunction> entry : IRRoot.funcs.entrySet()) {
            IRFunction irFunc = entry.getValue();
            new LoopDetector(irFunc).visitFunc();
            for (IRBlock irBlock : irFunc.blocks)
                blocks.put(irBlock, new AsmBlock("." + irFunc.name + "_" + irBlock.name, irBlock.loopDepth));
            AsmFunction asmFunc = new AsmFunction(entry.getKey(), blocks.get(irFunc.entryBlock), blocks.get(irFunc.exitBlock));
            funcs.put(irFunc, asmFunc);
            irFunc.paras.forEach(para -> asmFunc.paras.add(irToAsmReg(para)));
            asmRoot.funcs.add(asmFunc);
        }
        for (IRFunction irFunc : IRRoot.funcs.values())
            visitFunc(irFunc);
        return asmRoot;
    }

    private Register irToAsmReg(BaseOperand irOperand) {
        if (irOperand instanceof IR.Operand.Register || irOperand instanceof ParaOperand) {
            if (!regs.containsKey(irOperand))
                regs.put(irOperand, new VirtualReg(counter++, irOperand.type.size()));
            return regs.get(irOperand);
        } else if (irOperand instanceof StringOperand || irOperand instanceof GlobalVarOperand) {
            if (regs.containsKey(irOperand)) return regs.get(irOperand);
            int size = irOperand.type.resolvable() ? ((PointerIRType) irOperand.type).basicType.size() : irOperand.type.size();
            String name = (irOperand instanceof GlobalVarOperand) ? ((GlobalVarOperand) irOperand).name : "." + ((StringOperand) irOperand).name;
            GlobalReg globalReg = new GlobalReg(name, size / 8);
            regs.put(irOperand, globalReg);
            if (irOperand instanceof StringOperand) asmRoot.strings.put(globalReg, ((StringOperand) irOperand).value);
            else asmRoot.globalRegs.add(globalReg);
            return globalReg;
        } else if (irOperand instanceof IntOperand) {
            int value = ((IntOperand) irOperand).value;
            if (value == 0) return asmRoot.physicalRegs.get(0);
            if (liRegs.containsKey(value)) return liRegs.get(value);
            VirtualReg virtualReg = new VirtualReg(counter++, 4);
            currentBlock.addInst(new LiInst(currentBlock, virtualReg, new Imm(value)));
            liRegs.put(value, virtualReg);
            return virtualReg;
        } else if (irOperand instanceof BoolOperand) {
            return null;
        } else return asmRoot.physicalRegs.get(0);
    }

    private void visitFunc(IRFunction irFunc) {

    }
}
