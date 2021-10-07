import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import Parser.MxLexer;
import Parser.MxParser;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String inFile = null;
        if (args.length > 0) inFile = args[0];
        InputStream is = System.in;
        if (inFile != null) is = new FileInputStream(inFile);
        var input = new ANTLRInputStream(is);
        var lexer = new MxLexer(input);
        var tokens = new CommonTokenStream(lexer); //词法符号缓冲区
        var parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));
    }
}