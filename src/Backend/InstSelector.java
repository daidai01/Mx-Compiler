package Backend;

import Asm.Instruction.*;
import Asm.Operand.GlobalReg;
import Asm.Operand.Imm;
import Asm.Operand.Register;
import Asm.Operand.VirtualReg;
import Asm.Program.AsmBlock;
import Asm.Program.AsmFunction;
import Asm.Program.AsmRoot;
import IR.Instruction.*;
import IR.Instruction.BaseInst;
import IR.Operand.*;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;
import IR.Type.BaseIRType;
import IR.Type.ClassIRType;
import IR.Type.PointerIRType;
import Util.LoopDetector;

import java.util.ArrayList;
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
            irFunc.paras.forEach(para -> asmFunc.paras.add(irRegToAsm(para)));
            asmRoot.funcs.add(asmFunc);
        }
        for (IRFunction irFunc : IRRoot.funcs.values())
            visitFunc(irFunc);
        return asmRoot;
    }

    private void visitFunc(IRFunction irFunc) {
        AsmFunction asmFunc = funcs.get(irFunc);
        currentFunc = asmFunc;
        AsmBlock entryBlock = asmFunc.entryBlock;
        AsmBlock exitBlock = asmFunc.exitBlock;
        counter = 0;
        ArrayList<VirtualReg> calleeRegs = new ArrayList<>();
        Imm imm = new Imm(0, true);
        entryBlock.addInst(new ImmInst(entryBlock, asmRoot.physicalRegs.get(2), RegInst.AluOp.add, asmRoot.physicalRegs.get(2), imm));
        for (Register register : asmRoot.calleeRegs) {
            VirtualReg virtualReg = new VirtualReg(counter++, 4);
            calleeRegs.add(virtualReg);
            entryBlock.addInst(new MvInst(entryBlock, virtualReg, register));
        }
        VirtualReg virtualReg = new VirtualReg(counter++, 4);
        entryBlock.addInst(new MvInst(entryBlock, virtualReg, asmRoot.physicalRegs.get(1)));
        for (int i = 0; i < Integer.min(irFunc.paras.size(), 8); ++i)
            entryBlock.addInst(new MvInst(entryBlock, asmFunc.paras.get(i), asmRoot.physicalRegs.get(i + 10)));
        int offset = 0;
        for (int i = 8; i < irFunc.paras.size(); ++i) {
            entryBlock.addInst(new LdInst(entryBlock, asmRoot.physicalRegs.get(2), asmFunc.paras.get(i), new Imm(offset), irFunc.paras.get(i).type.size()));
            offset += 4;
        }
        for (IRBlock irBlock : irFunc.blocks) {
            AsmBlock asmBlock = blocks.get(irBlock);
            liRegs.clear();
            currentBlock = asmBlock;
            for (BaseInst inst = irBlock.headInst; inst != null; inst = inst.nxtInst)
                irInstToAsm(inst);
            for (IRBlock successor : irBlock.successors) {
                asmBlock.successors.add(blocks.get(successor));
                blocks.get(successor).pressessors.add(asmBlock);
            }
            asmFunc.blocks.add(asmBlock);
        }
        for (int i = 0; i < calleeRegs.size(); ++i)
            exitBlock.addInst(new MvInst(exitBlock, asmRoot.calleeRegs.get(i), calleeRegs.get(i)));
        exitBlock.addInst(new MvInst(exitBlock, asmRoot.physicalRegs.get(1), virtualReg));
        exitBlock.addInst(new ImmInst(exitBlock, asmRoot.physicalRegs.get(2), RegInst.AluOp.add, asmRoot.physicalRegs.get(2), new Imm(0)));
        exitBlock.addInst(new RetInst(exitBlock, asmRoot));
        asmFunc.counter = counter;
    }

    private Register irRegToAsm(BaseOperand irOperand) {
        if (irOperand instanceof IR.Operand.Register || irOperand instanceof ParaOperand) {
            if (!regs.containsKey(irOperand)) regs.put(irOperand, new VirtualReg(counter++, irOperand.type.size()));
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
            int value = ((BoolOperand) irOperand).value ? 1 : 0;
            if (value == 0) return asmRoot.physicalRegs.get(0);
            if (liRegs.containsKey(value)) return liRegs.get(value);
            VirtualReg virtualReg = new VirtualReg(counter++, 1);
            currentBlock.addInst(new LiInst(currentBlock, virtualReg, new Imm(value)));
            liRegs.put(value, virtualReg);
            return virtualReg;
        } else return asmRoot.physicalRegs.get(0);
    }

    private void irInstToAsm(BaseInst inst) {
        if (inst instanceof BinaryInst) {
            irBinaryToAsm(((BinaryInst) inst).op, ((BinaryInst) inst).leftOp, ((BinaryInst) inst).rightOp, irRegToAsm(inst.register), ((BinaryInst) inst).commutable);
        } else if (inst instanceof BitCastInst) {
            Register register = irRegToAsm(((BitCastInst) inst).operand);
            if (register instanceof GlobalReg)
                currentBlock.addInst(new LaInst(currentBlock, irRegToAsm(inst.register), register));
            else currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(inst.register), register));
        } else if (inst instanceof BranchInst) {
            BranchInst branchInst = (BranchInst) inst;
            if (branchInst.condition instanceof IR.Operand.Register && isBrReg(branchInst.block, (IR.Operand.Register) branchInst.condition) && (((IR.Operand.Register) branchInst.condition).defInst instanceof CmpInst)) {
                CmpInst cmpInst = (CmpInst) ((IR.Operand.Register) branchInst.condition).defInst;
                switch (cmpInst.op) {
                    case "<":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.blt, irRegToAsm(cmpInst.leftOp), irRegToAsm(cmpInst.rightOp), blocks.get(branchInst.trueBranch)));
                        break;
                    case ">":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.blt, irRegToAsm(cmpInst.rightOp), irRegToAsm(cmpInst.leftOp), blocks.get(branchInst.trueBranch)));
                        break;
                    case "<=":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.bge, irRegToAsm(cmpInst.rightOp), irRegToAsm(cmpInst.leftOp), blocks.get(branchInst.trueBranch)));
                        break;
                    case ">=":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.bge, irRegToAsm(cmpInst.leftOp), irRegToAsm(cmpInst.rightOp), blocks.get(branchInst.trueBranch)));
                        break;
                    case "==":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.beq, irRegToAsm(cmpInst.leftOp), irRegToAsm(cmpInst.rightOp), blocks.get(branchInst.trueBranch)));
                        break;
                    case "!=":
                        currentBlock.addInst(new BrInst(currentBlock, BrInst.BrOp.bne, irRegToAsm(cmpInst.leftOp), irRegToAsm(cmpInst.rightOp), blocks.get(branchInst.trueBranch)));
                }
                currentBlock.addInst(new JpInst(currentBlock, blocks.get(branchInst.falseBranch)));
            } else {
                currentBlock.addInst(new BzInst(currentBlock, BzInst.BzOp.beq, blocks.get(branchInst.falseBranch), irRegToAsm(branchInst.condition)));
                currentBlock.addInst(new JpInst(currentBlock, blocks.get(branchInst.trueBranch)));
            }
        } else if (inst instanceof CmpInst) {
            CmpInst cmpInst = (CmpInst) inst;
            if (isBrReg(inst.block, cmpInst.register)) return;
            VirtualReg virtualReg = new VirtualReg(counter, 4);
            switch (cmpInst.op) {
                case "<":
                    irSltToAsm(cmpInst.leftOp, cmpInst.rightOp, irRegToAsm(cmpInst.register));
                    break;
                case ">":
                    irSltToAsm(cmpInst.rightOp, cmpInst.leftOp, irRegToAsm(cmpInst.register));
                    break;
                case "<=":
                    counter++;
                    irSltToAsm(cmpInst.rightOp, cmpInst.leftOp, virtualReg);
                    currentBlock.addInst(new ImmInst(currentBlock, irRegToAsm(cmpInst.register), RegInst.AluOp.xor, virtualReg, new Imm(1)));
                    break;
                case ">=":
                    counter++;
                    irSltToAsm(cmpInst.leftOp, cmpInst.rightOp, virtualReg);
                    currentBlock.addInst(new ImmInst(currentBlock, irRegToAsm(cmpInst.register), RegInst.AluOp.xor, virtualReg, new Imm(1)));
                    break;
                case "==":
                    counter++;
                    irBinaryToAsm("^", cmpInst.leftOp, cmpInst.rightOp, virtualReg, true);
                    currentBlock.addInst(new SzInst(currentBlock, irRegToAsm(cmpInst.register), SzInst.SzOp.seq, virtualReg));
                    break;
                case "!=":
                    counter++;
                    irBinaryToAsm("^", cmpInst.leftOp, cmpInst.rightOp, virtualReg, true);
                    currentBlock.addInst(new SzInst(currentBlock, irRegToAsm(cmpInst.register), SzInst.SzOp.sne, virtualReg));
            }
        } else if (inst instanceof FunCallInst) {
            FunCallInst callInst = (FunCallInst) inst;
            for (int i = 0; i < Integer.min(callInst.paras.size(), 8); ++i) {
                Register register = irRegToAsm(callInst.paras.get(i));
                if (register instanceof GlobalReg)
                    currentBlock.addInst(new LaInst(currentBlock, asmRoot.physicalRegs.get(i + 10), register));
                else currentBlock.addInst(new MvInst(currentBlock, asmRoot.physicalRegs.get(i + 10), register));
            }
            int offset = 0;
            for (BaseOperand para : callInst.paras) {
                currentBlock.addInst(new StInst(currentBlock, asmRoot.physicalRegs.get(2), irRegToAsm(para), new Imm(offset), para.type.size() / 8));
                offset += 4;
            }
            currentFunc.paraOffset = Integer.max(currentFunc.paraOffset, offset);
            currentBlock.addInst(new CallInst(currentBlock, funcs.get(callInst.func), asmRoot));
            if (callInst.register != null)
                currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(callInst.register), asmRoot.physicalRegs.get(10)));
        } else if (inst instanceof GetElePtrInst) {
            GetElePtrInst gInst = (GetElePtrInst) inst;
            int size = gInst.type.size() / 8;
            Register targetIdx = new VirtualReg(counter++, 4);
            Register targetPtr = targetIdx;
            if (gInst.arrayOffset instanceof IntOperand) {
                int arrayIndex = ((IntOperand) gInst.arrayOffset).value;
                if (arrayIndex == 0) {
                    Register pointer = irRegToAsm(gInst.pointer);
                    if (pointer instanceof GlobalReg)
                        currentBlock.addInst(new LaInst(currentBlock, targetIdx, pointer));
                    else currentBlock.addInst(new MvInst(currentBlock, targetIdx, pointer));
                } else irBinaryToAsm("+", gInst.pointer, new IntOperand(arrayIndex * size, 32), targetIdx, true);
            } else {
                VirtualReg virtualReg = new VirtualReg(counter++, 4);
                irBinaryToAsm("*", gInst.arrayOffset, new IntOperand(size, 32), virtualReg, true);
                currentBlock.addInst(new RegInst(currentBlock, targetIdx, RegInst.AluOp.add, irRegToAsm(gInst.pointer), virtualReg));
            }
            int value = gInst.eleOffset.value;
            if (gInst.eleOffset != null && value != 0) {
                BaseIRType type = ((PointerIRType) gInst.pointer.type).basicType;
                if (type instanceof ClassIRType) value = ((ClassIRType) type).getOffset(value) / 8;
                else value = 0;
                targetPtr = new VirtualReg(counter++, 4);
                currentBlock.addInst(new ImmInst(currentBlock, targetPtr, RegInst.AluOp.add, targetIdx, new Imm(value)));
            }
            if (regs.containsKey(gInst.register))
                currentBlock.addInst(new MvInst(currentBlock, regs.get(gInst.register), targetPtr));
            else regs.put(gInst.register, targetPtr);
        } else if (inst instanceof JumpInst) {
            currentBlock.addInst(new JpInst(currentBlock, blocks.get(((JumpInst) inst).targetBlock)));
        } else if (inst instanceof LoadInst) {
            currentBlock.addInst(new LdInst(currentBlock, irRegToAsm(inst.register), irRegToAsm(((LoadInst) inst).address), new Imm(0), inst.register.type.size() / 8));
        } else if (inst instanceof MallocInst) {
            currentBlock.addInst(new MvInst(currentBlock, asmRoot.physicalRegs.get(10), irRegToAsm(((MallocInst) inst).size)));
            currentBlock.addInst(new CallInst(currentBlock, funcs.get(IRRoot.builtinFuncs.get("malloc")), asmRoot));
            currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(inst.register), asmRoot.physicalRegs.get(10)));
        } else if (inst instanceof MoveInst) {
            MoveInst moveInst = (MoveInst) inst;
            if (moveInst.src instanceof IntOperand)
                currentBlock.addInst(new LiInst(currentBlock, irRegToAsm(moveInst.register), new Imm(((IntOperand) moveInst.src).value)));
            else if (moveInst.src instanceof StringOperand || moveInst.src instanceof GlobalVarOperand)
                currentBlock.addInst(new LaInst(currentBlock, irRegToAsm(moveInst.register), irRegToAsm(moveInst.src)));
            else if (moveInst.src instanceof BoolOperand) {
                if (((BoolOperand) moveInst.src).value)
                    currentBlock.addInst(new LiInst(currentBlock, irRegToAsm(inst.register), new Imm(1)));
                else
                    currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(moveInst.register), asmRoot.physicalRegs.get(0)));
            } else
                currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(moveInst.register), irRegToAsm(moveInst.src)));
        } else if (inst instanceof ReturnInst) {
            if (((ReturnInst) inst).value != null) {
                Register value = irRegToAsm(((ReturnInst) inst).value);
                if (value instanceof GlobalReg)
                    currentBlock.addInst(new LaInst(currentBlock, asmRoot.physicalRegs.get(10), value));
                else currentBlock.addInst(new MvInst(currentBlock, asmRoot.physicalRegs.get(10), value));
            }
        } else if (inst instanceof StoreInst) {
            Register addrReg = irRegToAsm(((StoreInst) inst).address);
            if (addrReg instanceof GlobalReg) {
                VirtualReg virtualReg = new VirtualReg(counter++, 4);
                currentBlock.addInst(new LuiInst(currentBlock, virtualReg, new Imm(0)));
                currentBlock.addInst(new StInst(currentBlock, virtualReg, irRegToAsm(((StoreInst) inst).value), new Imm(0), ((StoreInst) inst).value.type.size() / 8));
            } else
                currentBlock.addInst(new StInst(currentBlock, addrReg, irRegToAsm(((StoreInst) inst).value), new Imm(0), ((StoreInst) inst).value.type.size() / 8));
        } else {
            Register register = irRegToAsm(((ZextInst) inst).src);
            if (register instanceof GlobalReg)
                currentBlock.addInst(new LaInst(currentBlock, irRegToAsm(inst.register), register));
            else currentBlock.addInst(new MvInst(currentBlock, irRegToAsm(inst.register), register));
        }
    }

    private void irBinaryToAsm(String op, BaseOperand src1, BaseOperand src2, Register register, boolean commutable) {
        RegInst.AluOp aluOp = null;
        boolean canbeImm = true;
        switch (op) {
            case "+":
                aluOp = RegInst.AluOp.add;
                break;
            case "-":
                aluOp = RegInst.AluOp.sub;
                break;
            case "*":
                aluOp = RegInst.AluOp.mul;
                canbeImm = false;
                break;
            case "/":
                aluOp = RegInst.AluOp.div;
                canbeImm = false;
                break;
            case "%":
                aluOp = RegInst.AluOp.rem;
                canbeImm = false;
                break;
            case "<<":
                aluOp = RegInst.AluOp.sll;
                break;
            case ">>":
                aluOp = RegInst.AluOp.sra;
                break;
            case "&":
                aluOp = RegInst.AluOp.and;
                break;
            case "|":
                aluOp = RegInst.AluOp.or;
                break;
            case "^":
                aluOp = RegInst.AluOp.xor;
        }
        if (canbeImm && commutable && (src1 instanceof IntOperand))
            currentBlock.addInst(new ImmInst(currentBlock, register, aluOp, irRegToAsm(src2), new Imm(((IntOperand) src1).value)));
        else if (canbeImm && (src2 instanceof IntOperand)) {
            if (aluOp == RegInst.AluOp.sub)
                currentBlock.addInst(new ImmInst(currentBlock, register, RegInst.AluOp.add, irRegToAsm(src1), new Imm(-1 * ((IntOperand) src2).value)));
            else
                currentBlock.addInst(new ImmInst(currentBlock, register, aluOp, irRegToAsm(src1), new Imm(((IntOperand) src2).value)));
        } else if (aluOp == RegInst.AluOp.mul && (src1 instanceof IntOperand)) {
            int power = isTwoPower(((IntOperand) src1).value);
            if (power == -1)
                currentBlock.addInst(new RegInst(currentBlock, register, aluOp, irRegToAsm(src1), irRegToAsm(src2)));
            else
                currentBlock.addInst(new ImmInst(currentBlock, register, RegInst.AluOp.sll, irRegToAsm(src2), new Imm(power)));
        } else if (aluOp == RegInst.AluOp.mul && (src2 instanceof IntOperand)) {
            int power = isTwoPower(((IntOperand) src2).value);
            if (power == -1)
                currentBlock.addInst(new RegInst(currentBlock, register, aluOp, irRegToAsm(src1), irRegToAsm(src2)));
            else
                currentBlock.addInst(new ImmInst(currentBlock, register, RegInst.AluOp.sll, irRegToAsm(src1), new Imm(power)));
        } else currentBlock.addInst(new RegInst(currentBlock, register, aluOp, irRegToAsm(src1), irRegToAsm(src2)));
    }

    private boolean isBrReg(IRBlock block, IR.Operand.Register register) {
        if (register.uses.size() == 1) for (BaseInst use : register.uses)
            if (use == block.tailInst) return true;
        return false;
    }

    private void irSltToAsm(BaseOperand src1, BaseOperand src2, Register register) {
        if (src2 instanceof IntOperand)
            currentBlock.addInst(new ImmInst(currentBlock, register, RegInst.AluOp.slt, irRegToAsm(src1), new Imm(((IntOperand) src2).value)));
        else
            currentBlock.addInst(new RegInst(currentBlock, register, RegInst.AluOp.slt, irRegToAsm(src1), irRegToAsm(src2)));
    }

    private int isTwoPower(int n) {
        int power = 0;
        while (n > 1) {
            if (n % 2 != 0) return -1;
            else {
                n /= 2;
                power++;
            }
        }
        return power;
    }
}
