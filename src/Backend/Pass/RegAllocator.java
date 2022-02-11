package Backend.Pass;

import Asm.Program.AsmRoot;

public class RegAllocator implements Pass {
    AsmRoot asmRoot;

    public RegAllocator(AsmRoot asmRoot){
        this.asmRoot=asmRoot;
    }

    @Override
    public void run() {

    }
}
