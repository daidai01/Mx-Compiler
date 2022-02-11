package Backend.Pass;

import Asm.Instruction.BaseInst;
import Asm.Operand.GlobalReg;
import Asm.Operand.PhysicalReg;
import Asm.Operand.Register;
import Asm.Program.AsmBlock;
import Asm.Program.AsmRoot;
import Asm.Program.AsmFunction;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AsmPrinter implements Pass {
    public PrintStream ps;
    public AsmRoot asmRoot;
    public ArrayList<AsmBlock> visits = new ArrayList<>();

    public AsmPrinter(PrintStream ps, AsmRoot asmRoot) {
        this.ps = ps;
        this.asmRoot = asmRoot;
    }

    @Override
    public void run() {
        ps.println("\t.text");
        for (AsmFunction func : asmRoot.funcs) {
            ps.println("\t.global\t" + func.name);
            ps.println("\t.p2align\t1");
            ps.println("\t.type\t" + func.name + ",@function");
            ps.println(func.name + ":");
            visits.clear();

            int blockCnt = 0;
            Queue<AsmBlock> blocks = new LinkedList<>();
            blocks.add(func.entryBlock);
            while (!blocks.isEmpty()) {
                AsmBlock block = blocks.poll();
                block.name = "." + func.name + "_b." + blockCnt;
                blockCnt++;
                for (AsmBlock blk : block.successors)
                    if (blk != null && !visits.contains(blk)) {
                        blocks.add(blk);
                        visits.add(blk);
                    }
            }

            visitBlock(func.entryBlock);
            for (AsmBlock block : visits)
                if (!visits.contains(block) && !block.hasPreBlock)
                    visitBlock(block);
            ps.println("\t.size\t" + func.name + ", .-" + func.name + "\n");
        }
        for (PhysicalReg reg : asmRoot.physicalRegs) {
            ps.println("\t.type\t" + reg.name + ",@object");
            ps.println("\t.section\t.bss");
            ps.println("\t.global\t" + reg.name);
            ps.println("\t.p2align\t2");
            ps.println(reg.name + ":");
            ps.println(".L" + reg.name + "$local:");
            ps.println("\t.word\t0");
            ps.println("\t.size\t" + reg.name + ", 4\n");
        }
        for (Map.Entry<GlobalReg, String> entry : asmRoot.strings.entrySet()) {
            GlobalReg reg = entry.getKey();
            String value = entry.getValue();
            ps.println("\t.type\t" + reg.name + ",@object");
            ps.println("\t.section\t.rodata");
            ps.println(reg.name + ":");
            String str = getString(value);
            ps.println("\t.asciz\t\"" + str + "\"");
            ps.println("\t.size\t" + reg.name + ", " + value.length() + "\n");
        }
    }

    private void visitBlock(AsmBlock block) {
        if (visits.contains(block)) throw new RuntimeException();
        visits.add(block);
        ps.println(block.name + ": ");
        for (BaseInst inst = block.headInst; inst != null; inst = inst.nxtInst)
            ps.println("\t" + inst.toString());
        if (block.nxtBlock != null) visitBlock(block.nxtBlock);
    }

    private String getString(String string) {
        String str = string.replace("\\", "\\\\");
        str = str.replace("\n", "\\n");
        str = str.replace("\0", "");
        str = str.replace("\t", "\\t");
        str = str.replace("\"", "\\\"");
        return str;
    }
}
