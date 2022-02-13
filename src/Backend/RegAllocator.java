package Backend;

import Asm.Program.AsmRoot;
import Backend.Pass.Pass;

public class RegAllocator implements Pass {
    AsmRoot asmRoot;

    public RegAllocator(AsmRoot asmRoot){
        this.asmRoot=asmRoot;
    }

    @Override
    public void run() {

    }
}
