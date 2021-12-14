package IR;

import IR.Instruction.*;
import IR.Program.Block;
import IR.Program.Function;
import IR.Program.Module;

public interface IRVisitor {
    //program
    void visit(Block it);

    void visit(Function it);

    void visit(Module it);

    //instruction
    void visit(AllocaInst it);

    void visit(BinaryInst it);

    void visit(BitCastInst it);

    void visit(BranchInst it);

    void visit(CmpInst it);

    void visit(FunCallInst it);

    void visit(LoadInst it);

    void visit(MoveInst it);

    void visit(PhiInst it);

    void visit(ReturnInst it);

    void visit(StoreInst it);
}
