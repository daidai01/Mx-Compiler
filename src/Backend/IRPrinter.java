package Backend;

import IR.Instruction.BaseInst;
import IR.Operand.GlobalVarOperand;
import IR.Operand.ParaOperand;
import IR.Operand.StringOperand;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;
import IR.Type.ClassIRType;
import IR.Type.PointerIRType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class IRPrinter{
    public PrintStream ps;
    public int symbolCnt = 0;
    public int blockCnt = 0;
    public ArrayList<IRBlock> visits = new ArrayList<>();

    public IRPrinter(PrintStream ps) {
        this.ps = ps;
    }

    public void run(IRRoot IRRoot) {
        for (IRFunction func : IRRoot.builtinFuncs.values()) {
            symbolCnt = 0;
            ps.println(funcHead(func, true));
        }
        for (Map.Entry<String, ClassIRType> entry : IRRoot.types.entrySet()) {
            ps.print("%struct." + entry.getKey() + " = " + "type {");
            int size = entry.getValue().members.size();
            for (int i = 0; i < size; ++i) {
                ps.print(entry.getValue().members.get(i).toString());
                if (i == size - 1) ps.print("}\n");
                else ps.print(", ");
            }
        }
        for (GlobalVarOperand gVar : IRRoot.gVars)
            ps.println("@" + gVar.name + " = global " + ((PointerIRType) gVar.type).basicType.toString() + " zeroinitializer, align " + gVar.type.size() / 8);
        for (Map.Entry<String, StringOperand> entry : IRRoot.constStrings.entrySet())
            ps.println("@" + entry.getKey() + " = private unamed_addr constant " + "[" + entry.getValue().value.length() + " x i8] c \"" + entry.getValue().toIRString() + "\", align 1");
        for (IRFunction func : IRRoot.funcs.values()) {
            symbolCnt = blockCnt = 0;
            visits.clear();
            ps.println(funcHead(func, false));

            Queue<IRBlock> blocks = new LinkedList<>();
            blocks.add(func.entryBlock);
            visits.add(func.entryBlock);
            while (!blocks.isEmpty()) {
                IRBlock block = blocks.poll();
                block.name = "b." + blockCnt;
                blockCnt++;
                for (IRBlock blk : block.successors) {
                    if (blk != null && !visits.contains(blk)) {
                        blocks.add(blk);
                        visits.add(blk);
                    }
                }
            }

            for (IRBlock block : visits) {
                block.phis.forEach(((register, inst) -> register.name = "" + symbolCnt++));
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
                    if (inst.register != null)
                        inst.register.name = "" + symbolCnt++;
            }
            for (IRBlock block : visits) {
                block.phis.forEach(((register, inst) -> ps.println("\t" + inst.toString())));
                for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
                    ps.println("\t" + inst.toString());
            }
            ps.println("}");
        }
    }

    private String funcHead(IRFunction func, boolean builtin) {
        String str = builtin ? "declare " : "define ";
        str += func.returnType + " @" + func.name + "(";
        int size = func.paras.size();
        if (size == 0) str += ")";
        else {
            for (int i = 0; i < size; ++i) {
                ParaOperand para = func.paras.get(i);
                para.name = "" + symbolCnt++;
                str += para.type.toString() + " " + para.toString();
                if (i == size - 1) str += ")";
                else str += ", ";
            }
        }
        if (!builtin) str += "{";
        return str;
    }
}