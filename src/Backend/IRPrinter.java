package Backend;

import IR.IRVisitor;
import IR.Instruction.*;
import IR.Program.Block;
import IR.Program.Function;
import IR.Program.Module;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

public class IRPrinter implements IRVisitor {
    public File file;
    public OutputStream out;
    public PrintWriter writer;

    @Override
    public void visit(Block it) {

    }

    @Override
    public void visit(Function it) {

    }

    @Override
    public void visit(Module it) {

    }

    @Override
    public void visit(AllocaInst it) {

    }

    @Override
    public void visit(BinaryInst it) {

    }

    @Override
    public void visit(BitCastInst it) {

    }

    @Override
    public void visit(BranchInst it) {

    }

    @Override
    public void visit(CmpInst it) {

    }

    @Override
    public void visit(FunCallInst it) {

    }

    @Override
    public void visit(LoadInst it) {

    }

    @Override
    public void visit(MoveInst it) {

    }

    @Override
    public void visit(PhiInst it) {

    }

    @Override
    public void visit(ReturnInst it) {

    }

    @Override
    public void visit(StoreInst it) {

    }
}
