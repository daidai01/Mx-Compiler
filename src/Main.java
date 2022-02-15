import AST.Program.RootNode;
import Asm.Program.AsmRoot;
import Backend.*;
import Backend.Pass.*;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import Frontend.SymbolCollector;
import Frontend.TypeCollector;
import IR.Program.IRRoot;
import Util.ErrorListener;
import Util.Scope.GlobalScope;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import Parser.MxLexer;
import Parser.MxParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String inFile = "testcases/codegen/e1.mx";
        try {
            InputStream input = new FileInputStream(inFile);
//            InputStream input = System.in;
            GlobalScope globalScope = new GlobalScope();
            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new ErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new ErrorListener());
            ParseTree parseTreeRoot = parser.program();
            ASTBuilder astBuilder = new ASTBuilder();
            RootNode ASTRoot = (RootNode) astBuilder.visit(parseTreeRoot);
            IRRoot IRRoot = new IRRoot();
            SymbolCollector symbolCollector = new SymbolCollector(globalScope, IRRoot);
            ASTRoot.accept(symbolCollector);
            TypeCollector typeCollector = new TypeCollector(globalScope);
            ASTRoot.accept(typeCollector);
            SemanticChecker semanticChecker = new SemanticChecker(globalScope, IRRoot);
            ASTRoot.accept(semanticChecker);

            //codegen
            IRBuilder irBuilder = new IRBuilder(globalScope, IRRoot);
            ASTRoot.accept(irBuilder);
            new MemToReg(IRRoot).run();
            new IRPrinter(IRRoot, new PrintStream("out.ll")).run();
            new PhiResolver(IRRoot).run();
            AsmRoot asmRoot = new InstSelector(IRRoot).run();
            new RegAllocator(asmRoot).run();
            new AsmPrinter(new PrintStream("output.s"), asmRoot).run();
        } catch (Error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}