import AST.Program.RootNode;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import Frontend.SymbolCollector;
import Frontend.TypeCollector;
import Util.ErrorListener;
import Util.Position;
import Util.Scope.GlobalScope;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import Parser.MxLexer;
import Parser.MxParser;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
//        String inFile = "text.mx";
        try {
//            InputStream input = new FileInputStream(inFile);
            InputStream input = System.in;
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
//            System.out.println(globalScope.containClass("A"));
            SymbolCollector symbolCollector = new SymbolCollector(globalScope);
//            System.out.println(globalScope.containClass("A"));
            ASTRoot.accept(symbolCollector);
            TypeCollector typeCollector = new TypeCollector(globalScope);
            ASTRoot.accept(typeCollector);
            SemanticChecker semanticChecker = new SemanticChecker(globalScope);
            ASTRoot.accept(semanticChecker);
        } catch (Error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}