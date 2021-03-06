package Asm.Program;

import Asm.Operand.GlobalReg;
import Asm.Operand.PhysicalReg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AsmRoot {
    public HashSet<AsmFunction> funcs = new HashSet<>();
    public HashSet<AsmFunction> builtinFuncs = new HashSet<>();
    public HashMap<GlobalReg, String> strings = new HashMap<>();
    public ArrayList<PhysicalReg> physicalRegs = new ArrayList<>();
    public HashSet<GlobalReg> globalRegs = new HashSet<>();
    public ArrayList<PhysicalReg> callerRegs = new ArrayList<>();
    public ArrayList<PhysicalReg> calleeRegs = new ArrayList<>();
    public ArrayList<PhysicalReg> kRegs = new ArrayList<>();

    public AsmRoot() {
        physicalRegs.add(new PhysicalReg("zero"));
        physicalRegs.add(new PhysicalReg("ra"));
        physicalRegs.add(new PhysicalReg("sp"));
        physicalRegs.add(new PhysicalReg("gp"));
        physicalRegs.add(new PhysicalReg("tp"));
        physicalRegs.add(new PhysicalReg("t0"));
        physicalRegs.add(new PhysicalReg("t1"));
        physicalRegs.add(new PhysicalReg("t2"));
        physicalRegs.add(new PhysicalReg("s0"));
        physicalRegs.add(new PhysicalReg("s1"));
        physicalRegs.add(new PhysicalReg("a0"));
        physicalRegs.add(new PhysicalReg("a1"));
        physicalRegs.add(new PhysicalReg("a2"));
        physicalRegs.add(new PhysicalReg("a3"));
        physicalRegs.add(new PhysicalReg("a4"));
        physicalRegs.add(new PhysicalReg("a5"));
        physicalRegs.add(new PhysicalReg("a6"));
        physicalRegs.add(new PhysicalReg("a7"));
        physicalRegs.add(new PhysicalReg("s2"));
        physicalRegs.add(new PhysicalReg("s3"));
        physicalRegs.add(new PhysicalReg("s4"));
        physicalRegs.add(new PhysicalReg("s5"));
        physicalRegs.add(new PhysicalReg("s6"));
        physicalRegs.add(new PhysicalReg("s7"));
        physicalRegs.add(new PhysicalReg("s8"));
        physicalRegs.add(new PhysicalReg("s9"));
        physicalRegs.add(new PhysicalReg("s10"));
        physicalRegs.add(new PhysicalReg("s11"));
        physicalRegs.add(new PhysicalReg("t3"));
        physicalRegs.add(new PhysicalReg("t4"));
        physicalRegs.add(new PhysicalReg("t5"));
        physicalRegs.add(new PhysicalReg("t6"));

        for (int i = 0; i < 32; ++i) {
            if (i == 1 || (5 <= i && i <= 7) || (10 <= i && i <= 17) || (28 <= i && i <= 31))
                callerRegs.add(physicalRegs.get(i));
            else if (i == 8 || i == 9 || (18 <= i && i <= 27))
                calleeRegs.add(physicalRegs.get(i));
        }

        kRegs.addAll(callerRegs);
        kRegs.addAll(calleeRegs);
        kRegs.remove(0); //ra
        kRegs.add(physicalRegs.get(1)); //ra
    }
}
