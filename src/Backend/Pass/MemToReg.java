package Backend.Pass;

import IR.Program.IRRoot;

public class MemToReg implements Pass {
    public IRRoot IRRoot;

    public MemToReg(IRRoot IRRoot) {
        this.IRRoot = IRRoot;
    }

    @Override
    public void run() {

    }
}
