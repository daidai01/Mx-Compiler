package Util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Position {
    private int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Token token) {
        this.row = token.getLine();
        this.col = token.getCharPositionInLine();
    }

    public Position(TerminalNode terminalNode) {
        this(terminalNode.getSymbol());
    }

    public Position(ParserRuleContext context) {
        this(context.getStart());
    }

    public int row() {
        return row;
    }

    public int column() {
        return col;
    }

    public String toString() {
        return row + "," + col;
    }
}