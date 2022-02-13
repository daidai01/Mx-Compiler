package Asm.Program;

import Asm.Operand.Register;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class AsmFunction {
    public String name;
    public ArrayList<Register> paras = new ArrayList<>();
    public HashSet<AsmBlock> blocks = new LinkedHashSet<>();
    public AsmBlock entryBlock = null;
    public AsmBlock exitBlock = null;
    public int paraOffset = 0;
    public int counter = 0;

    public AsmFunction(String name) {
        this.name = name;
    }

    public AsmFunction(String name, AsmBlock entryBlock, AsmBlock exitBlock) {
        this.name = name;
        this.entryBlock = entryBlock;
        this.exitBlock = exitBlock;
    }
}
